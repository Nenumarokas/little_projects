def is_number(word: str) -> bool:
    try:
        int(word)
    except:
        try:
            float(word)
        except:
            return False
    return True

def find_non_numeric_columns(data: list) -> list:
    new_data = [True] * len(data[0])
    for index in range(len(data[0])):
        if not all([is_number(i[index]) for i in data]):
            new_data[index] = False
    return [index for index, i in enumerate(new_data) if not i]

def normalize_data(data: list, index: int) -> list:
    thing = [int(i[index]) for i in data]
    for i in data:
        i[index] = str((int(i[index]) - min(thing))/(max(thing)-min(thing)))

    return data

file_name = 'train'

required = ['Id', 'LotShape', 'YearRemodAdd', 'LotArea', 'GrLivArea', 'GarageYrBlt', 'PoolQC', 'SaleCondition', 'SalePrice']
IDS = []
with open(f'csv/{file_name}.csv', 'r') as file:
    headers = file.readline()[:-1].split(';')
    for index, thing in enumerate(headers):
        if thing in required:
            IDS.append(index)
    data = [i[:-1].split(';') for i in file.readlines()]
    new_headers = [header for index, header in enumerate(headers) if index in IDS]
    data = [[a for index, a in enumerate(x) if index in IDS] for x in data]

garage = [int(i[5]) for i in data if is_number(i[5])]
for i in data:
    if i[5] != 'NA':
        i[5] = str((int(i[5]) - min(garage))/(max(garage)-min(garage)))
    else:
        i[5] = '0'

for i in data:
    i[6] = str(int(i[6] == 'NA'))

for i in [1, 3, 4]:
    data = normalize_data(data, i)
numbered = find_non_numeric_columns(data)

head = []
additions = []
for i in range(len(new_headers)):
    if i in numbered:
        column = [a[i] for a in data]
        onehot = [(a, column.count(a)) for a in list(set(column)) if column.count(a)/len(column)*100 > 5]
        onehot.append(('other', len(column)-sum([a[1] for a in onehot])))
        information_line = [datum[i] for datum in data]
        new_b = []
        for col in information_line:
            new_b.append([str(int(col == x[0])) for x in onehot])
        additions.append(new_b)
        head.extend([f'{new_headers[i]}_{a[0]}' for a in onehot])
    else:
        head.append(new_headers[i])


new_data = []
for i in range(len(data)):
    new_data.append(data[i][:2]+additions[0][i]+data[i][3:7]+additions[1][i]+data[i][8:])
print(head)

with open(f'csv/{file_name}.csv', 'r') as file:
    _ = file.readline()
    with open(f'csv/{file_name}_new.csv', 'w') as output:
        output.write(";".join(head)+'\n')
        for i in new_data:
            output.write(f'{";".join(i)}\n')
