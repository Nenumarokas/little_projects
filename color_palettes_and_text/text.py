from unidecode import unidecode
import numpy as np
import bs4 as bs
import traceback
import requests
import shutil
import spacy
import time
import os

def get_page_text(page):
    url = f'https://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles={page}&format=json'
    response = requests.get(url)
    if response.ok:
        try:
            data = response.json()
            page_id = list(data['query']['pages'].keys())[0]
            content = data['query']['pages'][page_id]['extract']
            content = content.split("<span id=\"References\">")[0]
            soup = bs.BeautifulSoup(content, 'html.parser')
            text = soup.get_text()
            return text
        except Exception:
            print(traceback.format_exc())
    return ''

def get_similiar(page, text_list, nlp):
    remove = []
    with open(f'{spacied_folder}/{page}', 'r', encoding = 'utf-8') as f:
        page_wiki = f.readline()[:-1]
        page_text = nlp(" ".join(f.readlines()))
    answer = []
    for index, text in enumerate(text_list):
        print(f'{index+1}/{len(text_list)}', end = '\r')
        with open(f'{spacied_folder}/{text}', 'r', encoding = 'utf-8') as f:
            curr_wiki = f.readline()[:-1]
            curr_text = " ".join(f.readlines())
            if len(curr_text) < 10:
                remove.append(f'{spacied_folder}/{text}')
                continue
            curr_text = nlp(curr_text)
            answer.append((page_wiki, curr_wiki, page_text.similarity(curr_text)))
    for removing in remove:
        os.remove(removing)
    answer.sort(key = lambda x: x[2], reverse=True)
    return answer[1:11]

if __name__ == '__main__':
    np.random.seed(15138161)
    cities = ['Kaunas', 'Milano', 'Lisboa']
    for city in cities:
        folder = f'{city}_texts'
        folder = f'{city}_lt_texts'
        if not os.path.exists(folder):
            home_dir = os.getcwd()
            os.mkdir(folder)
            with open(f'{city}_output.txt', 'r', encoding = 'utf-8') as f:
                pages = f.readlines()

            os.chdir(folder)
            for page in pages:
                text = get_page_text(page)
                filename = unidecode(page[3:])
                for char in ['\\', '/', ':', '*', '?', '\"', '<', '>', '|']:
                    filename = filename.replace(char, '')
                
                with open(f'{filename[:-1]}.txt', 'w', encoding = 'utf-8') as f:
                    f.write(f'{page}\n{text}')
                print(page)
            os.chdir(home_dir)

        nlp = spacy.load("en_core_web_lg")
        STOP_WORDS = spacy.lang.en.stop_words.STOP_WORDS
        nlp = spacy.load("lt_core_news_lg")
        STOP_WORDS = spacy.lang.lt.stop_words.STOP_WORDS

        spacied_folder = f'{folder}_spacied'
        if not os.path.exists(spacied_folder):
            os.mkdir(spacied_folder)
            for i in os.listdir(folder):
                with open(f'{folder}/{i}', 'r', encoding = 'utf-8') as f:
                    wiki = f.readline()
                    text = " ".join(f.readlines()).replace('\n', ' ')
                    text_no_stopwords = " ".join([token.lemma_ for token in nlp(text) if token.text.lower() not in STOP_WORDS])
                    with open(f'{spacied_folder}/{i}', 'w', encoding = 'utf-8') as output:
                        output.write(f'{wiki}\n')
                        output.write(text_no_stopwords)
        
        text_list = os.listdir(spacied_folder)
        chosen = text_list[int(np.random.random()*len(text_list))]
        if city == 'Kaunas':
            chosen = 'IV Kauno fortas.txt'
        print(f'Chosen page: {chosen}')
        print(f'Similiar pages according to the text:')
        for i in get_similiar(chosen, text_list, nlp):
            print(f'\t\"{i[1]}\" with a similiarity index of {i[2]:.4}')
        print()
