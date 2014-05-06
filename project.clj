(defproject site "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :source-paths ["src/clj"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [lib-noir "0.8.2"]
                 [compojure "1.1.6"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.5"]
                 [com.taoensso/timbre "3.1.6"]
                 [com.taoensso/tower "2.0.1"]
                 [markdown-clj "0.9.41"]
                 [environ "0.4.0"]
                 [clojurewerkz/cassaforte "1.3.0-beta11"]
                 [org.immutant/immutant "1.1.1"]
                 [com.draines/postal "1.11.1"]
                 [clj-time "0.7.0"]
                 [liberator "0.11.0"]
                 [crypto-random "1.2.0"]
                 [ring/ring-json "0.3.1"]
                 [cheshire "5.3.1"]
                 [hiccup "1.0.5"]
                 [clj-http "0.9.1"]]

  :repl-options {:init-ns core.repl}
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.4.0"]]
  :ring {:handler core.handler/app
         :init    core.handler/init
         :destroy core.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  true}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.1"]]
         :env {:dev true}}}
  :immutant {:context-path "/"
             :nrepl-port 11111
             :virtual-host "centipair.com"
             }
  :min-lein-version "2.0.0")
