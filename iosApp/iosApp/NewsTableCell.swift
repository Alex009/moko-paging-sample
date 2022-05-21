//
//  NewsTableCell.swift
//  iosApp
//
//  Created by Aleksey Mikhailov on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import MultiPlatformLibraryUnits

class NewsTableCell: UITableViewCell, Fillable {
    typealias DataType = Data
    
    struct Data {
        let title: String
        let caption: String
    }
    
    @IBOutlet var titleLabel: UILabel!
    @IBOutlet var captionLabel: UILabel!
    
    func fill(_ data: Data) {
        titleLabel.text = data.title
        captionLabel.text = data.caption
    }
}
