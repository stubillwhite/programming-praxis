(defproject programming-praxis "0.1.0-SNAPSHOT"

  :description "TODO"

  :url "TODO"

  :license { :name "Eclipse Public License"
             :url "http://www.eclipse.org/legal/epl-v10.html" }

  :repl-options { :port 4555 }

  :plugins [
             ;; Documentation
             [codox "0.6.4"] ]
  
  :dependencies [
                  ;; Core
                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/math.numeric-tower "0.0.4"]

                  ;; Logging
                  [com.taoensso/timbre "3.3.1"]

                  ;; Debugging
                  [org.clojure/tools.trace "0.7.8"]
                 
                  ;; Testing
                  [midje "1.6.3"] ]
  
  :profiles { :dev { :dependencies [ [org.clojure/tools.namespace "0.2.7"] ]
                     :source-paths ["dev"] } })
