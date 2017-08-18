
## Useful Commands

## restart the snmp master aget
sudo systemctl restart snmpd.service

## run the agent as standalone
sudo java -jar target/snmp-agent-0.1.0.jar

## Cluster 1
IP = 172.22.150.132
Hostname = mssav910
Domain = mssav910.local

## Cluster 2
IP = 172.22.150.133
Hostname = mssav920
Domain = mssav920.local

Account (read-only):
User = MMC
Password = Nutanix/4u