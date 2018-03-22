#!/usr/bin/env groovy

node {
    stage('Build') {
        sh '''
            home=`pwd`
            sudo apt-get update
            sudo apt-get install -y libpcre++-dev libgeoip-dev libxslt1-dev zlib1g zlib1g-dev zlibc  
            echo "download libluajit if not exist"
            if [ -f /usr/local/lib/libluajit-5.1.so.2.0.5 ]
            then echo "libluajit was installed already"
            else 
                wget http://luajit.org/download/LuaJIT-2.0.5.tar.gz
                tar xf LuaJIT-2.0.5.tar.gz
            fi
            wget 
            echo "download source code of nginx and additional modules (like lua-nginx-module)"
            for lst in `cat download.lst`
            do
            wget $lst
            done
            
            for i in *.tar.gz
            do
            tar xf $i
            rm $i
            done
            
            cd $home/LuaJIT-2.0.5 && sudo make && sudo mkdir /usr/local/include && sudo mkdir /usr/local/share/luajit-2.0.5 && sudo checkinstall -D -y --install=yes
            cd $home/nginx-1.13.10 && sudo ./configure --prefix=/opt/nginx --with-ld-opt="-Wl,-rpath,/usr/local/lib" --add-module=$home/ngx_devel_kit-0.3.0/ --add-module=$home/lua-nginx-module-0.10.11/ && sudo make -j2 && sudo checkinstall -D -y --install=no
        '''
    }  
}