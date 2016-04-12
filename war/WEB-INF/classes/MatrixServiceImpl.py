import sys

# TODO: move this to java for portability
sys.path.insert(0, '/Users/kiwi/Documents/workspace/Isochrome/war/WEB-INF/lib')
sys.path.insert(0, '/Users/kiwi/jython2.7.0/Lib/site-packages')
sys.path.insert(0, '/Users/kiwi/jython2.7.0/Lib')

# import Distance

from com.google.gwt.user.server.rpc import RemoteServiceServlet
from kiwi.project.isochrome.client import City, MatrixService, Matrix

from java.lang import Integer
from java.util import ArrayList

import requests
import unittest
import re

# from org.python.core import PyArray
# from jarray import array

KEY = "AIzaSyB8TLfJJQr9epuu28NDS6gkM9DlRP0jSJY"

class MatrixServiceImpl(RemoteServiceServlet, MatrixService):
    def getCity(self, cityName):
        return City(cityName, "code")

    def getMatrix(self, origins, destinations):
        matrix = requestMatrix(origins, destinations)

        result = [ArrayList(row) for row in matrix]
        return ArrayList(result)

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
def requestMatrix(origins, destinations, output="json", key=KEY):
    # SINGLETON = (len(origins) == 1)   # could be useful for singleton updates\
    # if ((len(origins) + len(destinations) >= 2000): raise RequestError('too long')
    origins = '|'.join(origins)
    destinations = '|'.join(destinations)

    params = "origins="+origins+"&"+"destinations="+destinations
    request = "http://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + params
    print "REQUEST: " + request

    result = requests.get(request).json()

    if not (result['status'] == 'OK'): raise RequestError(result['status'])

    # validate_origins(result, origins)
    # validate_destinations(result, destinations)
    result = extract_data(result)
    # result = invert(result)

    return result
