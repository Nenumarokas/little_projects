import traceback
import requests
import json
import os

def convert(wikis, name, country_code):
    with open(f'{name}_output.txt', 'a', encoding = 'utf-8') as outfile:
        for i in range(0, len(wikis), 19):

            try:
                inp = "|".join(wikipedia[i: i+19])
                response = requests.get(f"https://{country_code}.wikipedia.org/w/api.php?action=query&lllang=en&prop=langlinks&titles={inp}&format=json")

                print(i, inp, response.status_code)

                if (response.ok):
                    data = response.json()
                    pages = list(data["query"]["pages"].values())
                    for page in pages:
                        if "langlinks" in page:
                            parsed = "en:" + page["langlinks"][0]["*"]
                            outfile.write(parsed + "\n")
                            print(parsed)

                            outfile.flush()
                            os.fsync(outfile.fileno())
            except Exception:
                print(traceback.format_exc())

if __name__ == '__main__':
    name = 'Lisboa'
    country_code = 'pt'
    with open(f'{name}_output.txt', 'w') as f: f.write('')
    with open(f'{name}.json', 'r', encoding = 'utf-8') as f:
        answer = json.load(f)

    wikipedia = [i['tags']['wikipedia'] for i in answer['elements'] if i.get('tags') and i['tags'].get('wikipedia')]
    #wikipedia = [i for i in wikipedia if i[:2] == "en"]
    print(len(wikipedia))
    wikipedia = list(set(wikipedia))

    for i in wikipedia:
        print(i)
    print(len(wikipedia))

    convert(wikipedia, name, country_code)