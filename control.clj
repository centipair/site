(defcluster :default-cluster
  :clients [
    {:host "centipair.com" :user "devasia"}
  ]
  :ssh-options "-p 827" )

(def production-code-path "/home/devasia/webapps/core")

(deftask :date "echo date on cluster"  []
  (ssh "date"))

(deftask :deploy 
  "Deploys code to server"
  []
  ;;(local "git push origin master")
  (ssh 
   (run 
    (cd production-code-path 
        (run "git pull origin master")))))
