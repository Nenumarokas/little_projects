from requests import get as reqget
from qwikidata.linked_data_interface import get_entity_dict_from_api
from MySQLdb import connect

PAGE_ID_LINK = "https://en.wikipedia.org/w/api.php?format=json&action=query&titles="
INFO_LINK = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=pageviews&pageids="
CITY = 'kaunas' # lisbon, milan, kaunas
LANGUAGE = "lt" # PT, IT, LT

def possible_languages(q42_dict: dict, unique: str, language: str) -> str:
    """
    searches three possible wikipedia pages
    """
    wikis = ['enwiki', f'{language}wiki', 'commonswiki']
    wiki_info = None
    for wiki_info in wikis:
        try:
            wiki_info = q42_dict['sitelinks'][wiki_info][unique]
        except Exception:
            continue
        if wiki_info is not None:
            break
    return wiki_info

def onehot_tourism(types: list, line: dict) -> list:
    """
    performs one-hot encoding on tourism column
    """
    new_line = [line[0], line[2], line[3], line[4]]
    addition = [int(i == line[5]) for i in types]
    addition[-1] = int(sum(addition) == 0)
    new_line.extend(addition)
    return new_line

def get_page_visits(line: list) -> int:
    """
    returns the page visits of any working page of the three possible
    """
    wikidata_label = ''
    #wiki_url = ''
    visits_this_year = 0
    if line[3] != '':
        q42_dict = get_entity_dict_from_api(line[3])
        wikidata_label = possible_languages(q42_dict, 'title', LANGUAGE)
        if wikidata_label is None:
            try:
                wikidata_label = q42_dict['labels'][list(q42_dict['labels'])[0]]['value']
            except Exception:
                pass
        #wiki_url = possible_languages(q42_dict, 'url', LANGUAGE)
        try:
            page_id = list(reqget(f"{PAGE_ID_LINK}{wikidata_label}").json()['query']['pages'])[0]
            info = reqget(f"{INFO_LINK}{page_id}").json()
            visits = info['query']['pages'][page_id]['pageviews'].values()
            visits_this_year = sum(i for i in visits if i is not None)
        except Exception:
            pass
    return visits_this_year

if __name__ == '__main__':
    conn = connect('127.0.0.1', user='root')
    cursor = conn.cursor()
    cursor.execute('use big_data_information')
    cursor.execute(f'SELECT * FROM {CITY}')
    data = cursor.fetchall()

    tourism_types = ['apartment', 'artwork', 'attraction', 'gallery', 'guest_house', 'hostel',\
                     'hotel', 'information', 'motel', 'museum', 'picnic_site', 'viewpoint', 'other']
    new_header = ['id', 'coord_x', 'coord_y']\
               + [f"tourism_{i}" for i in tourism_types]\
               + ['visits_this_year']

    data = [onehot_tourism(tourism_types, datum) for datum in data]

    x_coords = [float(i[1]) for i in data]
    y_coords = [float(i[2]) for i in data]

    x_coord_offset = min(x_coords)
    x_coord_max = max(x_coords)-x_coord_offset
    y_coord_offset = min(y_coords)
    y_coord_max = max(y_coords)-y_coord_offset

    for datum in data:
        datum[2] = str((float(datum[2])-y_coord_offset)/y_coord_max)
        datum[1] = str((float(datum[1])-x_coord_offset)/x_coord_max)

    data = [datum+[get_page_visits(datum)] for datum in data]

    data = [datum[:-1] + [str(datum[-1]/max(i[-1] for i in data))] for datum in data]
    for_k = [[i for index, i in enumerate(datum) if index != 3] for datum in data]

    new_name = f'{CITY}_reformated'
    cursor.execute(f'DROP TABLE IF EXISTS {new_name};')
    tourism_string = ", ".join([f"{i} int(1)" for i in new_header[3:-1]])
    cursor.execute(f'CREATE TABLE {new_name}(id int(5) PRIMARY KEY, x_coord varchar(10),'
                 + f'y_coord varchar(10), {tourism_string}, page_visits varchar(10));')
    for i in for_k:
        cursor.execute(f'INSERT INTO {new_name} VALUES({", ".join(map(str, i))});')
        conn.commit()
