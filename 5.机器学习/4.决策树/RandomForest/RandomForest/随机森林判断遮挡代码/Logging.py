#-*- coding: UTF-8 -*-
import logging
import logging.handlers


logger = logging.getLogger(__name__)
logger.setLevel(level = logging.INFO)
handler = logging.handlers.TimedRotatingFileHandler("debug.log", when='D', interval=1, backupCount=40)
handler.setLevel(logging.INFO)
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler.setFormatter(formatter)
console = logging.StreamHandler()
console.setLevel(logging.INFO)
logger.addHandler(handler)
logger.addHandler(console)



