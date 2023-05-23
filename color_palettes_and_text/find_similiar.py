from palettes import get_palette_colors
from sklearn.cluster import KMeans
from unidecode import unidecode
import numpy as np
import cv2 as cv
import itertools
import warnings
import shutil
import os

def find_nearest(palettes, selected_palette):
    distances = [(np.linalg.norm(selected_palette - palette[1]), palette[0]) for palette in palettes]
    distances.sort(key = lambda x: x[0])
    return distances

def get_object_info(obj):
    palette = get_palette_colors(cv.imread(f'{folder}/{obj}/palette.jpg'))
    with open(f'{folder}/{obj}/text.txt', 'r', encoding = 'utf-8') as f:
        name = f.readline()[:-1]
        return (name, palette)

def find_closest_palettes(selected_palette, folder):
    palettes = [get_object_info(obj) for obj in os.listdir(folder)]
    palettes = [(palette[0], [np.array(i)/255 for i in palette[1]]) for palette in palettes]
    palettes = [(palette[0], np.array(list(itertools.chain(*palette[1])))) for palette in palettes]

    nearest = find_nearest(palettes, selected_palette)[1:11]
    for i in nearest:
        print(i[1])
    exit()

if __name__ == '__main__':
    np.random.seed(1984)
    curent_path = os.getcwd()

    selected_city = ['Kaunas', 'Lisboa', 'Milano'][0]
    folder = f'{selected_city}_images'
    selected_ID = int(np.random.random()*len(os.listdir(folder)))
    selected = os.listdir(folder)[selected_ID]
    print(f'Selected: {selected}')
    selected_item = f'{folder}/{selected}'
    if os.path.exists(f'{selected_item}/text.txt'):
        with open(f'{selected_item}/text.txt', 'r', encoding = 'utf-8') as f:
            print(f'Wikipedia: {f.readline()}')

    palette = get_palette_colors(cv.imread(f'{selected_item}/palette.jpg'))
    palette = np.array(palette)/255
    palette = np.array(list(itertools.chain(*palette)))
                       
    find_closest_palettes(palette, folder)
    cv.imshow('a', palette)
    cv.waitKey()

