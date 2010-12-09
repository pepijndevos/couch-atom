# Couch Atom #

An Atom interface for updating CouchDB documents.

compare-and-set! maps to Clutch's update-document, returning false if there was a conflict. swap! and reset! are implemented on top of this and work as expected. @couch-atom maps to get-document.

A small example of the problem it solves:

    (with-db "test" (create-document {:bar 1} "foo"))
    {:_id "foo", :_rev "1-0b26fd22294459c090b330f5cae3e3b3", :bar 1}
    
    (let [atm (couch-atom "foo")
          f #(with-db "test"
               (swap! atm update-in [:bar] inc))]
      (pcalls f f f f f f))
    ({:_id "foo", :_rev "1-0b26fd22294459c090b330f5cae3e3b3", :bar 2}
     {:_id "foo", :_rev "5-bc028aa00fa316e5313c50046c604492", :bar 6}
     {:_id "foo", :_rev "3-46efb9106635c0f41157cff977957d3d", :bar 4}
     {:_id "foo", :_rev "6-0074859f1a59c82f5bf2edb1912b98e2", :bar 7}
     {:_id "foo", :_rev "4-5489e8ea6c9eda4c1eb53e2b16a0b895", :bar 5}
     {:_id "foo", :_rev "2-b70f9436fb127ce4ae6b477960092a2b", :bar 3})
    
    (with-db "test" @(couch-atom "foo"))
    {:_id "foo", :_rev "7-3769a53cee7cbe8956843d24267d2d38", :bar 7}

## Known Issues ##

 * Watchers and validators are not yet implemented.
 * Regular atoms might suffer a bit performance wise when using my versions of swap!, reset! and compare-and-set!
