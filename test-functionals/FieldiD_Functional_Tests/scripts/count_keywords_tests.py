'''
This script counts the total number of keywords (in resources and tests directories)
and total number of tests (in tests directory) and outputs to an XML file

Author: hlatif
Date: Nov 9, 2017
'''
import sys
import os
from fnmatch import fnmatch
from robot.api import TestData
from robot.parsing.model import ResourceFile
from robot.parsing.model import TestCaseFile
import xml.etree.cElementTree as ET

'''
copy and paste from http://effbot.org/zone/element-lib.htm#prettyprint
it basically walks your tree and adds spaces and newlines so the tree is
printed in a nice way
'''
def indent(elem, level=0):
  i = "\n" + level*"  "
  if len(elem):
    if not elem.text or not elem.text.strip():
      elem.text = i + "  "
    if not elem.tail or not elem.tail.strip():
      elem.tail = i
    for elem in elem:
      indent(elem, level+1)
    if not elem.tail or not elem.tail.strip():
      elem.tail = i
  else:
    if level and (not elem.tail or not elem.tail.strip()):
      elem.tail = i

def print_suite(suite):
    print ('Suite:', suite.name)
    for test in suite.testcase_table:
        print ('-', test.name)
    for keyword in suite.keyword_table:
        print ('-', keyword.name)
    for child in suite.children:
        print_suite(child)
    print (len(suite.keyword_table.keywords), 'keywords')
    print (len(suite.testcase_table.tests), 'tests')
    print('##############################################')

def print_xml_resource(suite,root):
    testsuite = ET.SubElement(root, "suite", name=str(suite.name), keywords=str(len(suite.keyword_table.keywords)))
    for keyword in suite.keyword_table:
        kw = ET.SubElement(testsuite, "keyword")
        kw.text = str(keyword.name)
    return len(suite.keyword_table.keywords)

def print_xml_tests(suite,root):
    testsuite = ET.SubElement(root, "suite", name=str(suite.name), keywords=str(len(suite.keyword_table.keywords)), tests=str(len(suite.testcase_table.tests)))
    for keyword in suite.keyword_table:
        kw = ET.SubElement(testsuite, "keyword")
        kw.text = str(keyword.name)

    for test in suite.testcase_table:
            tst = ET.SubElement(testsuite, "test")
            tst.text = str(test.name)
    return len(suite.keyword_table.keywords), len(suite.testcase_table.tests)

def parse_resource_files(root):
    total_keywords = 0
    cwd = os.getcwd()
    pattern = "*.robot"
    dir = '%s/../resources' % (cwd)
    for path, dirs, files in os.walk(dir):
        for name in files:
            if fnmatch(name, pattern):
                absPath = os.path.join(path, name)
                print (absPath)
                suite = ResourceFile(source='%s' % absPath).populate()
                print_suite(suite)
                # print xml suite
                keywords = print_xml_resource(suite,root)
                total_keywords = total_keywords + keywords
                indent(root)
    return total_keywords

def parse_test_files(root):
    total_keywords = 0
    total_tests = 0
    cwd = os.getcwd()
    pattern = "*.robot"
    dir = '%s/../tests' % (cwd)
    for path, dirs, files in os.walk(dir):
        for name in files:
            if fnmatch(name, pattern):
                absPath = os.path.join(path, name)
                print (absPath)
                suite = TestCaseFile(source='%s' % absPath).populate()
                print_suite(suite)
                # print xml suite
                keywords, tests = print_xml_tests(suite,root)
                total_keywords = total_keywords + keywords
                total_tests = total_tests + tests
                indent(root)
    return total_keywords, total_tests

def main():
    cwd = os.getcwd()
    root = ET.Element("tests")
    resource_keywords = parse_resource_files(root)
    os.chdir(cwd)
    test_keywords, tests = parse_test_files(root)
    total_keywords = resource_keywords + test_keywords
    print ("total # keywords:", resource_keywords + test_keywords)
    print ("total # tests:", tests)

    tree = ET.ElementTree(root)
    root.set('tests', str(tests))
    root.set('keywords', str(total_keywords))
    tree.write("results.xml", xml_declaration=True, encoding='utf-8', method="xml")

if __name__ == "__main__":
    main()