import ast
import warnings
import numpy as np
import matplotlib.pyplot as plt
from yellowbrick.cluster import KElbowVisualizer, SilhouetteVisualizer
from sklearn.cluster import KMeans
from typing import Tuple

ELBOW_LENGTH = 10
N_CLUSTERS = 5
warnings.filterwarnings("ignore")

def find_nearest(centroids, point):
    distances = [(np.linalg.norm(centroid - point[1:-1]), index)\
                 for index, centroid in enumerate(centroids)]
    return min(distances, key = lambda x: x[0])

def get_csv_data() -> Tuple[list, list]:
    with open('csv/train_new.csv', 'r', encoding = 'utf-8') as file:
        data = [line[:-1].split(';') for line in file.readlines()]
        data = [[ast.literal_eval(part) for part in datum] for datum in data[1:]]
    return data, [parts[1:-1] for parts in data]
    
def draw_elbow(data_for_clustering: list) -> None:
    km = KMeans(random_state=42)
    visualiser = KElbowVisualizer(km, k=(1, ELBOW_LENGTH))
    visualiser.fit(np.array(data_for_clustering))
    visualiser.show()

def draw_silhouettes(data_for_clustering: list) -> None:
    for i in [3, 4, 5, 6]:
        km = KMeans(n_clusters=i, init='k-means++', n_init=10, max_iter=100, random_state=42)
        visualizer = SilhouetteVisualizer(km)
        visualizer.fit(np.array(data_for_clustering))
        visualizer.show()

def draw_bar(averages: list):
    x_axis = [i[0] for i in averages]
    y_axis = [i[1] for i in averages]

    plt.bar(x_axis, y_axis)
    plt.title('Average price per cluster')
    plt.ylabel('Price')
    plt.xlabel('Cluster ID')
    plt.show(block = False)
    plt.pause(2)
    plt.close()

def calculate_points(centroids) -> Tuple[list, list]:
    assignments = [[] for i in centroids]
    averages = [[] for i in centroids]
    for datum in data:
        distances = find_nearest(centroids, datum)
        assignments[distances[1]].append(datum)
        averages[distances[1]].append(distances[0])
    return assignments, averages

if __name__ == '__main__':
    data, data_for_clustering = get_csv_data()
    draw_elbow(data_for_clustering)
    draw_silhouettes(data_for_clustering)

    #for N_CLUSTERS in range(1, 6):
    kmeans = KMeans(n_clusters=N_CLUSTERS)
    kmeans.fit(data_for_clustering)
    assignments, averages = calculate_points(kmeans.cluster_centers_)

    average_price = []
    for index, assignment in enumerate(assignments):
        print(f'Nodes in this cluster: {len(assignment)}')
        kmeans_cost = sum(averages[index])/len(averages[index])
        price = sum([int(i[-1]) for i in assignment])//len(assignment)
        print(f'average price: {price}')

        average_price.append((index, price))
        print()

    draw_bar(average_price)