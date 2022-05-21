//
//  LoaderTableCell.swift
//  iosApp
//
//  Created by Aleksey Mikhailov on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import MultiPlatformLibraryUnits

class LoaderTableCell: UITableViewCell, Fillable {
    typealias DataType = Void
    
    @IBOutlet var activityIndicator: UIActivityIndicatorView!
    
    func fill(_ data: Void) {
        activityIndicator.startAnimating()
    }
}
