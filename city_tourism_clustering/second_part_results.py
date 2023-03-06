import os
import warnings
import numpy as np
from yellowbrick.cluster import KElbowVisualizer, SilhouetteVisualizer
from sklearn.cluster import KMeans
from MySQLdb import connect
from typing import Tuple

warnings.filterwarnings("ignore")
np.random.seed(100)
CITIES = [('lisbon', 5), ('milan', 4), ('kaunas', 6)]
ELBOW_LENGTH = 15
N_CLUSTERS = 4

def draw_silhouettes(data_for_clustering: list):
    for i in [3, 4, 5, 6, 7, 8]:
        kmeans = KMeans(n_clusters=i, init='k-means++', n_init=10, max_iter=100, random_state=42)
        visualizer = SilhouetteVisualizer(kmeans)
        visualizer.fit(np.array(data_for_clustering))
        visualizer.show()

def draw_elbow(data_for_clustering: list) -> None:
    kmeans = KMeans(random_state=42)
    visualiser = KElbowVisualizer(kmeans, k=(1, ELBOW_LENGTH))
    visualiser.fit(np.array(data_for_clustering))
    visualiser.show()

def find_nearest(centroids: np.ndarray, point: list) -> Tuple[float, int]:
    distances = [(np.linalg.norm(centroid - np.array(point[1:])), index)\
                 for index, centroid in enumerate(centroids)]
    return min(distances, key = lambda x: x[0])

def calculate_points(centroids: np.ndarray) -> Tuple[list, list]:
    assignments = [[] for i in centroids]
    for datum in data:
        distances = find_nearest(centroids, datum)
        assignments[distances[1]].append(datum)
    return assignments

def give_coordinates(data: list) -> list:
    if data[-1] == '': data[-1] = 'Unknown'
    return [str(datum) for index, datum in enumerate(data) if index in [0, 2, 3, 6]]

def give_final_string(data: list) -> list:
    " - "
    if data[-1] == '': data[-1] = 'Unknown'
    final = [str(datum) for index, datum in enumerate(data) if index in [0, 2, 3, 6]]
    final[-1] += " - " + str(data[5])
    return final

def data_to_file(clusters: list, city: str, primary_data: list):
    directory = f'{city}_locations'
    output_data = []
    for cluster in clusters:
        cluster_data = []
        for line in cluster:
            primary = primary_data[line[0]-1]
            if primary[-1] == '':primary[-1] = 'Unknown'
            cluster_data.append([primary[0]]+primary[2:4]+[str(primary[-1]+" - "+primary[-2])])
        output_data.append(cluster_data)

    if not os.path.exists(directory):
        os.mkdir(directory)

    for index, cluster in enumerate(output_data):
        file_name = f'{directory}/{city}_cluster_{index}.csv'
        with open(file_name, 'w', encoding = 'utf-8') as output:
            output.write('id,x_coord,y_coord,name\n')
            for line in cluster:
                output.write(f'{", ".join(map(str, line))}\n')

def find_similiar(clusters: list, random_point: list, city: str, city_center: list) -> list:
    randomized_point_cluster = find_nearest(city_center, random_point)[1]
    cursor.execute(f'SELECT * FROM {city}')
    primary_data = cursor.fetchall()
    primary_data = list(map(list, primary_data))

    distances = [(np.linalg.norm(np.array(point[1:]) - np.array(random_point[1:])), point)\
                 for point in clusters[randomized_point_cluster]]
    distances.sort()
    final = [x for x in distances if x[1][0]!=random_point[0]]
    indeces = [i[1][0] for i in final[:10]]
    new_data = [primary_data[index-1] for index in indeces]
    print(indeces)
    return new_data

if __name__ == '__main__':
    conn = connect('127.0.0.1', user='root')
    cursor = conn.cursor()
    cursor.execute('use big_data_information')

    city_centers = []
    city_clusters = []
    for city_part in CITIES:
        city = city_part[0]
        n_clusters = city_part[1]
        cursor.execute(f'SELECT * FROM {city}_reformated')
        data = cursor.fetchall()
        
        cursor.execute(f'SELECT * FROM {city}')
        primary_data = cursor.fetchall()
        primary_data = list(map(list, primary_data))

        data = [[float(col) if index in [1, 2, len(datum)-1] else col for index, col in enumerate(datum)] for datum in data]
        for_kmeans = [i[1:] for i in data]

        """draw_elbow(for_kmeans)
        draw_silhouettes(for_kmeans)
        exit()"""

        kmeans = KMeans(n_clusters=n_clusters)
        kmeans.fit(for_kmeans)
        
        clusters = [[] for i in kmeans.cluster_centers_]
        for i in data:
            distance = find_nearest(kmeans.cluster_centers_, i)
            clusters[distance[1]].append(i)
        
        data_to_file(clusters, city, primary_data)
        city_clusters.append(clusters)
        city_centers.append(kmeans.cluster_centers_)

    
    wanted_id = 209
    #random_point = data[int(np.random.random()*len(data))]
    random_point = data[wanted_id-1]
    random_point_data = give_coordinates(primary_data[wanted_id-1])
    
    print(random_point_data)
    print(f'Randomized point is: {random_point}')
    print(f'More: {give_coordinates(primary_data[wanted_id-1])}')

    similiar = []
    for index, cluster in enumerate(city_clusters):
        city = CITIES[index][0]
        print(f'Similiar in: {city}:')
        similiar.extend(find_similiar(cluster, random_point, city, city_centers[index]))


    DIRECTORY = 'suggestion'
    if not os.path.exists(DIRECTORY):
        os.mkdir(DIRECTORY)
    with open(f'{DIRECTORY}/first.csv', 'w', encoding = 'utf-8') as output:
        output.write('id,x_coord,y_coord,name\n')
        output.write(",".join(random_point_data)+'\n')
    with open(f'{DIRECTORY}/offers.csv', 'w', encoding = 'utf-8') as output:
        output.write('id,x_coord,y_coord,name\n')
        for final in similiar:
            print(give_final_string(final))
            output.write(",".join(give_final_string(final))+'\n')