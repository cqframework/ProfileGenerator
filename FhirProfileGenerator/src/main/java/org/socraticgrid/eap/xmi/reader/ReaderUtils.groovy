package org.socraticgrid.eap.xmi.reader

import java.util.List;

import groovy.util.Node;
import groovy.xml.QName


/**
 * Helper utility class for the parsing of XMI 2.1 UML models.
 * 
 * @author cnanjo
 *
 */
class ReaderUtils {

	public static String getLocalName(def nodeName) {
		if(nodeName instanceof QName) {
			nodeName.getLocalPart()
		} else {
			nodeName
		}
	}
	
	public static void findNodes(Node node, List<Node> found, String nodeName) {
		String name = ReaderUtils.getLocalName(node.name())
		if(name == nodeName) {
			found.add(node);
		}
		node.children().each {
			it -> 
			if(it instanceof Node) {
				findNodes(it,found, nodeName)
			}
		}
	}
	
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
}
