# from kiwi.project.isochrome.interfaces import DistanceType
import requests
import unittest
import re

KEY = "AIzaSyB8TLfJJQr9epuu28NDS6gkM9DlRP0jSJY"

# def validate_origins(result, origins):
#     result_origins = [ s.replace(",", "") for s in result['origin_addresses'] ]
#     result_origins = [ s.replace(" ", "+") for s in result_origins ]
#
#     for i in range(len(origins)):
#         if not (re.finditer(origins[i], result_origins[i])): raise RequestError('invalid origins returned')
#
# def validate_destinations(result, destinations):
#     result_destinations = [ s.replace(",", "") for s in result['destination_addresses'] ]
#     result_destinations = [ s.replace(" ", "+") for s in result_origins ]
#
#     for i in range(len(destinations)):
#         if not(re.finditer(destinations[i], result_destinations[i])): raise RequestError('invalid destinations returned')

def extract_data(result):
    rows = result['rows']
    rows = [ extract_row(row) for row in rows]
    return rows

def extract_row(row):
    return [ e['duration']['value'] for e in row['elements']]

# put origins on the x axis
def invert(matrix):
    inverted = [[None]*len(matrix)]*len(matrix[0])

    for row in range(len(matrix)):
        for col in range(len(matrix[0])):
            inverted[col][row] = matrix[row][col]

    return inverted


class Error(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
        return repr(self.value)

class RequestError(Error):
    pass

# request distance data from origins to destinations, both in form ["City+Name+ST",...]
def requestMatrix(self, origins, destinations, output="json"):
    # SINGLETON = (len(origins) == 1)   # could be useful for singleton updates\
    # if ((len(origins) + len(destinations) >= 2000): raise RequestError('too long')
    origins = '|'.join(origins)
    destinations = '|'.join(destinations)

    params = "origins="+origins+"&"+"destinations="+destinations+"&"+"key="+self.key
    request = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + params
    result = requests.get(request).json()

    if not (result['status'] == 'OK'): raise RequestError(result['status'])

    # validate_origins(result, origins)
    # validate_destinations(result, destinations)
    result = extract_data(result)
    result = invert(result)

    return result






# origin and each destination a string in the form: Seattle+WA
# def singleton_request(origin, destinations, key=KEY, output="json"):
#     destinations = '|'.join(destinations)
#     if (len(destinations) >= 2000): raise RequestError('too long')
#
#     result = api_request([origin], destinations, key, output)
#     # matrix is useless for singleton origin
#     result['row'] =  result['rows'][0]['elements']
#     return result


# def matrix_request(origins, destinations, key=KEY, output="json"):
#     origins = '|'.join(origins)
#     destinations = '|'.join(destinations)
#
#     if ((len(origins) + len(destinations) >= 2000): raise RequestError('too long')
#
#     return api_request(origins, destinations, key, output)

#
# class TestRequests(unittest.TestCase):
#     def test_singleton_request(self):
#
#     def test_matrix_request(self):
#
#
#     def test_massive_request(self):
#         with self.assertRaises(DestinationsError):
#             big_string = "".join(['c' for i in range(2000)])
#             make_request('', big_string)
#
#     def test_invalid_request(self):
#         with self.assertRaises(RequestError):
#             make_request('', [])
#
# # call when you need to split into multiple requests
# def overload(origin, destinations, key=KEY, output="json"):
#     try:
#         make_request(origin, destinations, key=KEY, output="json")
#     except DestinationsError:
#         ds1 = destinations[:len(destinations)/2]
#         ds2 = destinations[len(destinations)/2:]
#
#         return concat(overload(origin, ds1), overload(origin, ds2))
#
# def concat(r1, r2):
#     if not (r1['origin_addresses'][0] == r2['origin_addresses'][0]): return
#
#     new =
#
#
#
# def test_request():
#     return make_request("Seattle+WA", ["Portland+OR", "San+Fransisco+CA", "Boise+ID"])
#
# if __name__ == '__main__':
#     unittest.main()
