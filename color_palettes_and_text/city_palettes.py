from palettes import get_city_palette
import numpy as np
import cv2 as cv
import os
cities = ['Kaunas', 'Lisboa', 'Milano']

if __name__ == '__main__':
    for city in cities:
        print(city)
        cv.imshow(f'{city}', get_city_palette(city))
    cv.waitKey()