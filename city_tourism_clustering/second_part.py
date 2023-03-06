"""
Lisabona
https://nominatim.openstreetmap.org/ui/search.html?q=Lisbon
5400890
118 perfect calls
The data gathering took 3736.0921592712402 seconds


Milanas
https://nominatim.openstreetmap.org/ui/search.html?q=Milan
44915
256 perfect calls
The data gathering took 7113.8599972724915 seconds


Kaunas
https://nominatim.openstreetmap.org/ui/search.html?q=Kaunas
1567544
279 perfect calls
The data gathering took 5970.11158156395 seconds
"""

import os
import time
from MySQLdb import connect
import requests
import matplotlib.pyplot as plt
import xml.etree.ElementTree as ET
from shapely.geometry import Polygon, shape, box

def plot_city(poly: Polygon):
    plt.plot(*poly.exterior.xy)
    for interiorRings in poly.interiors:
        plt.plot(*interiorRings.xy)
    plt.pause(3)
    plt.close()

def get_polygon(city_ID: str) -> Polygon:
    url = "https://nominatim.openstreetmap.org/details.php?osmtype=R&osmid=" + city_ID +"&class=boundary&addressdetails=1&hierarchy=0&group_hierarchy=1&format=json&polygon_geojson=1"
    r = requests.get(url)
    jsonResult = r.json()
    geom = jsonResult['geometry']
    polygon = Polygon(shape(geom))
    return polygon

def plot_polygon(city_poly: Polygon, step: float):
    box_coords = []
    (minx, miny, maxx, maxy) = city_poly.bounds
    xCur, yCur = minx, miny
    while xCur < maxx:
        yCur = miny
        while yCur < maxy:
            b = box(xCur, yCur, xCur+step, yCur+step)
            if b.intersects(city_poly):
                plt.plot(*b.exterior.xy, 'r')
                #print(f'({yCur}, {xCur}) - ({yCur+step}, {xCur+step})')
                #print(round(yCur, 4))
                box_coords.append([str(round(x, 4)) for x in [xCur, yCur, xCur+step, yCur+step]])
            else:
                plt.plot(*b.exterior.xy, 'b')
            yCur += step
        xCur += step
    return box_coords

def get_box_information(coords: str, city: str, id: int):
    url =f'https://overpass-api.de/api/map?bbox={coords}'
    r = requests.get(url)
    response = str(r)[11:-2]
    print(r)
    # examples to process XML: https://towardsdatascience.com/processing-xml-in-python-elementtree-c8992941efd2
    tree =  ET.ElementTree(ET.fromstring(r.content))
    root = tree.getroot()
    for node in root.iter('node'):
        for tag in node.iter('tag'):
            if(tag.attrib['k'] == 'tourism'):
                coord_x = node.attrib['lat']
                coord_y = node.attrib['lon']
                answer = ["==========", node.attrib['lat'] + "|" + node.attrib['lon']]
                for tag2 in node.iter('tag'):
                    answer.append(tag2.attrib)
                #if any(ans['k'] == 'wikidata' for ans in answer if type(ans) == dict):
                    # pasiimti 5-10 populiariausių vietų iš miesto ir įvertinti ar visos yra duombazėj
                    # kiekvienam kvadratui ir jo responsui sukurti po XML failą
                    # duomenų bazėje išsaugoti XML failo nuorodą
                    # kiekvienam objektui kuris turi wikidata, ištraukti svarbiausią informaciją
                    # jeigu neturi, bandyti ieškoti pagal pavadinimą
                new_dict = {'coord_x': coord_x, 'coord_y': coord_y}
                new_dict.update(dict([(ans['k'], ans['v']) for ans in answer if type(ans) == dict]))
                send_to_database(city, new_dict, id)
    return response

def send_to_database(name: str, data: dict, id: int):
    conn = connect('127.0.0.1', user='root')
    cursor = conn.cursor()
    string_data = ''
    try:
        new_string = str(data['name'])
        string_data = new_string.replace('\'', '')
    except:
        pass
    tourism = data['tourism']
    wikidata = ""
    try: 
        wikidata = data['wikidata']
    except:
        pass
    cursor.execute('use big_data_information')
    insert = f"INSERT INTO {name} VALUES\
        (NULL, '{id}', '{data['coord_x']}', '{data['coord_y']}', '{wikidata}', '{tourism}', '{string_data}');"
    #print(insert)
    cursor.execute(insert)
    conn.commit()

def print_coordinates(name: str, coords: list):
    with open(f'coordinates//{name}_coordinates.txt', 'w') as file:
        for coord in coords:
            file.write(','.join(coord)+'\n')

def do_stuff(city):
    name = city[0]
    ID = city[1]
    step = 0.01
    print(name)
    if not os.path.isfile(f'coordinates//{name}_coordinates.txt'):
        p3 = get_polygon(ID)
        coords = plot_polygon(p3, step)
        """plt.title(name)
        plt.show(block = False)
        plt.pause(3)
        plt.close()"""
        print_coordinates(name, coords)
        #get_box_information(coords)
    else:
        print('file already exists')

def check_singular(id: int, city: tuple):
    with open(f'coordinates//{city[0]}_coordinates.txt', 'r') as file:
        line = file.readlines()[id-1]
        get_box_information(line, city[0], id-1)

if __name__ == '__main__':
    conn = connect('127.0.0.1', user='root')
    cursor = conn.cursor()
    cursor.execute('use big_data_information')

    cities = [('Lisbon', '5400890'), ('Milan', '44915'), ('Kaunas', '1567544')]
    
    for city in cities:
        plot_city(get_polygon(city[1]))
        first = time.time()
        do_stuff(city)
        print(f'{city[0]} took {round(time.time() - first, 4)} seconds to complete\n')

    """check_singular(13, cities[0])

    exit()"""

    """# http://overpass-api.de/command_line.html
    times = []
    for city in [cities[-1]]:
        cursor.execute(f'DROP TABLE IF EXISTS {city[0]};')
        table = f'CREATE TABLE {city[0]}(id int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY, box_id int(5), coord_x varchar(10), coord_y varchar(10), wikidata varchar(30), tourism varchar(100), name varchar(100));'
        cursor.execute(table)
        with open(f'coordinates//{city[0]}_coordinates.txt', 'r') as file:
            first = time.time()
            lines = file.readlines()
            print(f'{city[0]}:')
            for index, line in enumerate(lines):
                print(f'Box no. {index+1}')
                counter = 0
                while (True):
                    if get_box_information(line, city[0], index+1) == '200':
                        break
                    print(f'retry NO {counter}')
                    counter += 1
                    time.sleep(1)
                time.sleep(1)
            print(f'The data gathering took {time.time() - first} seconds')
            times.append(time.time() - first)
            break
    for index, i in enumerate(cities[1:]):
        print(f'To get data from {i[0]} it took {times[index]} seconds')"""