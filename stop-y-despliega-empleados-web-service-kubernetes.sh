kubectl delete deployment empleados-web
kubectl delete service empleados-web-service
kind load docker-image empleados-web
kubectl apply -f emp_web_kub_dpl_srv.yml
sleep 3
kubectl get pods