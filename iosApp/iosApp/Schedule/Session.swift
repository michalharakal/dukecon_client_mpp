import UIKit

class Session {
    
    //MARK: Properties
    
    var name: String
    var speaker: String

    //MARK: Initialization
    
    init?(name: String, speaker: String) {
        
        // The name must not be empty
        guard !name.isEmpty else {
            return nil
        }
      
        // Initialize stored properties.
        self.name = name
        self.speaker = speaker
    }
}
