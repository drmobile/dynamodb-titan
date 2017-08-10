import sys
import urllib.request
import urllib.error


def wait_for_internet_connection(host, port):
    while True:
        try:
            urllib.request.urlopen('http://' + host + ':' + port, timeout=1)
            print("test2")
            return
        except (urllib.error.URLError, urllib.error.HTTPError) as e:
            if type(e) is urllib.error.HTTPError:
                return
            pass

if __name__ == '__main__':
    wait_for_internet_connection(sys.argv[1], sys.argv[2])
