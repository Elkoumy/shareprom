
import os
def parse_results(dir_to_log):
    print("parsing the results")
    os.system('cat '+dir_to_log+' > FG_log/DFG.log | python argument-stream-decipher.py')