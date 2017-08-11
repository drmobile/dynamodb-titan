import sys
import urllib.request
import urllib.error
from time import sleep


def wait_for_internet_connection(host, port):
    target = host + ':' + port
    print("Wait for {} ready".format(target))
    count = 0
    while True:
        try:
            urllib.request.urlopen('http://' + host + ':' + port, timeout=1)
            return target
        except Exception as e:
            dots = ''
            for i in range(3):
                dots += '.' if i <= count % 3 else ' '
            print("Still waiting for {} {}".format(target, dots), end='\r')
            count += 1
            if type(e) is urllib.error.HTTPError:
                return target
            sleep(1)
            pass


if __name__ == '__main__':
    ret = wait_for_internet_connection(sys.argv[1], sys.argv[2])
    print('\n{} is ready'.format(ret))
