spark-submit \
--conf spark.driver.extraJavaOptions="-Djna.library.path=/tmp/so/" \
--conf spark.executor.extraJavaOptions="-Djna.library.path=/tmp/so/" \
--class org.sinoptik.simple sharedobject-research_2.11-1.0.jar

export LD_LIBRARY_PATH=/opt/rh/devtoolset-7/root/usr/lib:/usr/local/lib:/usr/lib:/home/sinoptik/anaconda3/pkgs/libstdcxx-ng-9.3.0-hd4cf53a_

nm libraryName.so | grep procedureName
nm -D -C libraryName.so
# -C for C++ name-demangling
# -D dynamic

objdump -T libraryName.so