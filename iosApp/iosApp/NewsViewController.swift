//
//  NewsViewController.swift
//  iosApp
//
//  Created by Aleksey Mikhailov on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryUnits

class NewsViewController: UIViewController {
    @IBOutlet private var tableView: UITableView!
    @IBOutlet private var activityIndicator: UIActivityIndicatorView!
    @IBOutlet private var msgTitleLabel: UILabel!
    @IBOutlet private var msgCaptionLabel: UILabel!
    @IBOutlet private var msgButton: UIButton!
    
    private var viewModel: NewsViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel = NewsViewModel().start()
        
        let dataSource = TableUnitsSourceKt.default(for: tableView)
        
        viewModel.state.addObserver { [weak self] state in
            guard let state = state else { return }
            let stateKs = ResourceStateKs(state)
            self?.bindState(stateKs, dataSource: dataSource)
        }
    }
    
    private func bindState(
        _ state: ResourceStateKs<NSArray, StringDesc>,
        dataSource: TableUnitsSource
    ) {
        tableView.isHidden = state.success(false, or: true)
        bindItemsArray(
            dataSource: dataSource,
            items: state.success({ $0! }, or: [])
        )
        
        activityIndicator.isHidden = state.loading(false, or: true)
        
        let isMessageHidden: Bool
        let messageTitle: String
        let messageCaption: String
        let buttonTitle: String
        switch(state) {
        case .empty(_):
            isMessageHidden = false
            messageTitle = NSLocalizedString("empty_title", comment: "")
            messageCaption = NSLocalizedString("empty_caption", comment: "")
            buttonTitle = NSLocalizedString("refresh_btn", comment: "")
        case .failed(let error):
            isMessageHidden = false
            messageTitle = NSLocalizedString("error_title", comment: "")
            messageCaption = error.error?.localized() ?? ""
            buttonTitle = NSLocalizedString("retry_btn", comment: "")
        default:
            isMessageHidden = true
            messageTitle = ""
            messageCaption = ""
            buttonTitle = ""
        }
        
        msgTitleLabel.isHidden = isMessageHidden
        msgTitleLabel.text = messageTitle
        msgCaptionLabel.isHidden = isMessageHidden
        msgCaptionLabel.text = messageCaption
        msgButton.isHidden = isMessageHidden
        msgButton.setTitle(buttonTitle, for: .normal)
    }
    
    private func bindItemsArray(
        dataSource: TableUnitsSource,
        items: NSArray
    ) {
        let units: [TableUnitItem] = items.map { item in
            mapUnitItem(item as! NewsViewModelUnitItem)
        }
        dataSource.unitItems = units
    }
    
    private func mapUnitItem(
        _ item: NewsViewModelUnitItem
    ) -> TableUnitItem {
        let unitKs = NewsViewModelUnitItemKs(item)
        
        switch(unitKs) {
        case .newsItem(let item):
            return UITableViewCellUnit<NewsTableCell>(
                data: NewsTableCell.Data(
                    title: item.title,
                    caption: item.caption
                ),
                itemId: Int64(item.id)
            )
        }
    }
    
    @IBAction
    func onMsgButtonPressed() {
        guard let state = viewModel.state.value else { return }
        let stateKs = ResourceStateKs(state)
        
        switch(stateKs) {
        case .empty(_): viewModel.onRefreshPressed()
        case .failed(_): viewModel.onRetryPressed()
        default: return
        }
    }
}
