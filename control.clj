(defcluster :default-cluster
  :clients [
    {:host "centipair.com" :user "devasia"}
  ]
  :ssh-options "-p 827" )

(def production-code-path "/home/devasia/webapps/site")

(deftask :date "echo date on cluster"  []
  (ssh "date"))

(deftask :deploy 
  "Deploys code to server from git repo"
  []
  (ssh 
   (run 
    (cd production-code-path 
        (run "git reset --hard")
        (run "git pull origin master")))))
