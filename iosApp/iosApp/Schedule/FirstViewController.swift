//
//  FirstViewController.swift
//  iosApp
//
//  Created by Michal Harakal on 12.08.19.
//  Copyright © 2019 Michal Harakal. All rights reserved.
//

import UIKit
import dukecon

class FirstViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    private let myArray: NSArray = ["First","Second","Third"]
    private var myTableView: UITableView!
    private var repository : DomainConferenceRepository!
    private var events: [DomainEvent] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let barHeight: CGFloat = UIApplication.shared.statusBarFrame.size.height
        let displayWidth: CGFloat = self.view.frame.width
        let displayHeight: CGFloat = self.view.frame.height
        
        myTableView = UITableView(frame: CGRect(x: 0, y: barHeight, width: displayWidth, height: displayHeight - barHeight))
        myTableView.register(UITableViewCell.self, forCellReuseIdentifier: "MyCell")
        myTableView.dataSource = self
        myTableView.delegate = self
        self.view.addSubview(myTableView)
        let api =  DomainApplicationStorage()
        
        repository = DukeconDataKtorRepository(  endPoint: "https://www.apachecon.com/acna19/s/rest/", conferenceId: "acna2019.json", settings:  api    )
        repository.update{
            print($0)
            self.events = self.repository.getEvents()
            self.myTableView.reloadData()
            return
        }
        

    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("Num: \(indexPath.row)")
        print("Value: \(events[indexPath.row])")
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return events.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyCell", for: indexPath as IndexPath)
        
        cell.textLabel!.text = "\(events[indexPath.row].title)"
        return cell
}
}
