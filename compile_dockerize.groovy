#!/usr/bin/env groovy

node {
    stage('Build') {
        sh '''
            home=`pwd`
            sudo apt-get update
            sudo apt-get install -y libpcre++-dev libgeoip-dev libxslt1-dev zlib1g zlib1g-dev zlibc  
            echo "download, compile and install libluajit if not exist"
            if [ -f /usr/local/lib/libluajit-5.1.so.2.0.5 ]
            then 
                echo "libluajit was installed already"
                wget http://luajit.org/download/LuaJIT-2.0.5.tar.gz
                tar xf LuaJIT-2.0.5.tar.gz
                rm LuaJIT-2.0.5.tar.gz
                cd $home/LuaJIT-2.0.5 && sudo make && sudo mkdir -p /usr/local/include && sudo mkdir -p /usr/local/share/luajit-2.0.5 && sudo checkinstall -D -y --install=no
                cp $home/LuaJIT-2.0.5/luajit_2.0.5-1_amd64.deb $home/
                cd $home/
            else 
                wget http://luajit.org/download/LuaJIT-2.0.5.tar.gz
                tar xf LuaJIT-2.0.5.tar.gz
                rm LuaJIT-2.0.5.tar.gz
                cd $home/LuaJIT-2.0.5 && sudo make && sudo mkdir -p /usr/local/include && sudo mkdir -p /usr/local/share/luajit-2.0.5 && sudo checkinstall -D -y --install=yes
                cp $home/LuaJIT-2.0.5/luajit_2.0.5-1_amd64.deb $home/
                cd $home/
            fi
            wget https://raw.githubusercontent.com/vadmiralov87/task/master/download.lst
            echo "download source code of nginx and additional modules (like lua-nginx-module)"
            for lst in `cat download.lst`
            do
            wget $lst
            done
            
            echo "untar and delete"

            for i in *.tar.gz
            do
            tar xf $i
            rm $i
            done
            
            echo "build nginx with lua-nginx-module"
            cd $home/nginx-1.13.10 && sudo ./configure --prefix=/opt/nginx --with-ld-opt="-Wl,-rpath,/usr/local/lib" --add-module=$home/ngx_devel_kit-0.3.0/ --add-module=$home/lua-nginx-module-0.10.11/ && sudo make -j2 && sudo checkinstall -D -y --install=no
            cp $home/nginx-1.13.10/nginx_1.13.10-1_amd64.deb $home/
            cd $home
        '''
    }

    stage('Dockerize') {
        sh '''
            wget https://raw.githubusercontent.com/vadmiralov87/task/master/Dockerfile
            wget https://raw.githubusercontent.com/vadmiralov87/task/master/nginx.conf
            wget https://raw.githubusercontent.com/vadmiralov87/task/master/index.html
            sudo docker build . -t nginx:latest
            sudo docker tag nginx vadmiralov87/nginx
            sudo docker login -u $username -p $password
            sudo docker push vadmiralov87/nginx
        '''
    }
}