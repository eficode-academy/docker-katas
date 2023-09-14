import os
import socket

def get_system_info():
    # Get the host name
    host_name = socket.gethostname()
    
    # Get the OS name
    os_name = os.name
    if os_name == 'posix':
        os_name = os.uname().sysname
    elif os_name == 'nt':
        os_name = 'Windows'
    
    return host_name, os_name

if __name__ == "__main__":
    host_name, os_name = get_system_info()
    print(f"Host Name: {host_name}")
    print(f"OS Name: {os_name}")