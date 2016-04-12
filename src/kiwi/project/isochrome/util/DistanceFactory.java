package kiwi.project.isochrome.util;

import kiwi.project.isochrome.interfaces.DistanceType;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class DistanceFactory {
    private PyObject distanceClass;

    public DistanceFactory() {
    	PythonInterpreter interpreter = new PythonInterpreter();
    	PySystemState sys = Py.getSystemState();
        sys.path.append(new PyString("/usr/local/lib/python2.7/site-packages"));
    	
        interpreter.exec("from Distance import *");
        distanceClass = interpreter.get("Distance");
        
        // interpreter.close();
    }
    
    public DistanceType create() {
		 PyObject distanceObject = distanceClass.__call__(new PyString("AIzaSyB8TLfJJQr9epuu28NDS6gkM9DlRP0jSJY"));
		 return (DistanceType)distanceObject.__tojava__(DistanceType.class);
    }
}
