from sklearn.cluster import KMeans
from unidecode import unidecode
import numpy as np
import cv2 as cv
import warnings
import shutil
import os

warnings.filterwarnings("ignore")
cities = ['Kaunas', 'Lisboa', 'Milano']
PALETTE_LENGTH = 10

def get_palette_colors(image):
    return [image[50][i] for i in range(int(image.shape[0]/2), image.shape[1], image.shape[0])]

def get_city_palette(city):
    current_path = os.getcwd()
    os.chdir(f'{city}_images')
    city_colors = []
    for folder in os.listdir():
        image = cv.imread(f'{folder}\palette.jpg')
        city_colors.extend(get_palette_colors(image))
    average_palette = calculate_average_palette(city_colors, PALETTE_LENGTH)
    os.chdir(current_path)
    #cv.imwrite(f'{city}_palette.jpg', average_palette)
    return average_palette

def palette_for_kmeans(palette):
    normalized = [(p[0], int(p[1]/palette[-1][1])) for p in palette]
    palette_kmean = []
    for pixel in normalized:
        for _ in range(pixel[1]):
            palette_kmean.append(pixel[0])
    return palette_kmean

def read_palette(image):
    size = image.shape
    const = size[0]/size[1]
    new_size = 600
    if max(image.shape) > new_size:
        image = cv.resize(image, (new_size, int(new_size*const)))
    #cv.imshow('currently working on:', image)

    img = image.copy()
    Z = img.reshape((-1,3))
    Z = np.float32(Z)
    criteria = (cv.TERM_CRITERIA_EPS + cv.TERM_CRITERIA_MAX_ITER, PALETTE_LENGTH, 1.0)
    K = PALETTE_LENGTH
    _,label,center=cv.kmeans(Z,K,None,criteria,PALETTE_LENGTH,cv.KMEANS_RANDOM_CENTERS)
    center = np.uint8(center)
    res = center[label.flatten()]
    img = res.reshape((img.shape))
    #cv.imshow('res2', img)

    color_palette = []
    for color in center:
        mask = cv.inRange(img, color, color)
        count = cv.countNonZero(mask)
        color_palette.append((color, count))

    color_palette.sort(key = lambda x: x[1], reverse=True)

    palette_image = np.zeros([100,100*PALETTE_LENGTH,3],dtype=np.uint8)
    for index, color in enumerate(color_palette):
        palette_image[:,index*100:100*(index+1)] = color[0]
        
    #cv.imshow('palette', palette_image)
    #cv.waitKey()
    #cv.destroyAllWindows()

    return color_palette

def calculate_average_palette(average_palette, color_count):
    kmeans = KMeans(n_clusters=color_count, random_state=42)
    kmeans.fit(average_palette)

    centers = [[0, center] for center in kmeans.cluster_centers_]
    for color in average_palette:
        cluster = find_nearest(kmeans.cluster_centers_, color)
        centers[cluster[1]][0] += 1
    centers.sort(key = lambda x: x[0])

    palette_image = np.zeros([100,100*color_count,3],dtype=np.uint8)
    for index, color in enumerate([center[1] for center in centers]):
        palette_image[:,index*100:100*(index+1)] = color.astype(np.int32)
        
    #cv.imshow('palett2', palette_image)
    #cv.waitKey()
    return palette_image

def find_nearest(centroids, point):
    distances = [(np.linalg.norm(centroid - point[1:-1]), index)\
                 for index, centroid in enumerate(centroids)]
    return min(distances, key = lambda x: x[0]) 

if __name__ == '__main__':
    city = cities[2]
    os.chdir(f'{city}_images')
    for object in os.listdir():
        #object = os.listdir()[0]
        last_dir = os.getcwd()
        print(object)
        os.chdir(object)
        files = os.listdir()
        got_palette = False
        if len(files) > 1:
            average_palette = []
            for index, image_file in enumerate([file for file in files if file.endswith('.jpg') and file != 'palette.jpg']):
                extension = image_file.split('.')[-1]
                new_name = f'a{index}.{extension}'
                os.rename(image_file, new_name)
                image = cv.imread(new_name)
                palette = read_palette(image)
                average_palette.extend(palette_for_kmeans(palette))
                cv.waitKey()
            palette = calculate_average_palette(average_palette)
            got_palette = True
            cv.imwrite('palette.jpg', palette)
        os.chdir(last_dir)
        if not got_palette:
            shutil.rmtree(object)

        