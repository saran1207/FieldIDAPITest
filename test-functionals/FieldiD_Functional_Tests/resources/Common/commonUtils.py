import os
import sys
import pdb

from robot.libraries.BuiltIn import BuiltIn

def get_absolute_path(path):
    return os.path.abspath(path)

def get_webdriver_instance():
    se2lib = BuiltIn().get_library_instance('ExtendedSelenium2Library')
    return se2lib._current_browser()

def get_current_url():
    driver = get_webdriver_instance()
    return driver.current_url

def debug_robot():
    for attr in ('stdin', 'stdout', 'stderr'):
        setattr(sys, attr, getattr(sys, '__%s__' % attr))
pdb.set_trace()