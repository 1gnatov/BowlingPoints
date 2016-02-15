# BowlingPoints

See working example at http://bowling.my2i.ru


For run project at any host:
1. Compile project (javac app/App.java)
2. Run project (java app/App)
3. You can use API on port 8182 via HTTP with GET on page /calc (example: http://127.0.0.1/calc:8182)


For access via web:
1. Install any webserver (for example apache  or nginx)
2. Setup any ajax page with request on /calc:8182 or on /calc:80 + proxy on webserver


Example html page: example.html
Example nginx config:
    server {
        listen       127.0.0.1:80;
        server_name  bowling.example.com;
        charset utf-8;
        location / {
                root /var/www/sites/bowling.example.com;
        }
        location /calc {
                proxy_pass  http://127.0.0.1:8182;
        }
    }
