//
//  ResourceStateExt.swift
//  iosApp
//
//  Created by Aleksey Mikhailov on 21.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import MultiPlatformLibrary

extension ResourceStateKs {
    func success<R>(
        _ onSuccess: (T?) -> R,
        or onOther: R
    ) -> R {
        switch(self) {
        case .success(let data): return onSuccess(data.data)
        default: return onOther
        }
    }
    
    func success<R>(
        _ onSuccess: R,
        or onOther: R
    ) -> R {
        return success({ _ in onSuccess }, or: onOther)
    }
    
    func loading<R>(
        _ onLoading: R,
        or onOther: R
    ) -> R {
        switch(self) {
        case .loading(_): return onLoading
        default: return onOther
        }
    }
    
    func empty<R>(
        _ onEmpty: R,
        or onOther: R
    ) -> R {
        switch(self) {
        case .empty(_): return onEmpty
        default: return onOther
        }
    }
    
    func failed<R>(
        _ onFailed: (E?) -> R,
        or onOther: R
    ) -> R {
        switch(self) {
        case .failed(let error): return onFailed(error.error)
        default: return onOther
        }
    }
    
    func failed<R>(
        _ onFailed: R,
        or onOther: R
    ) -> R {
        return failed({ _ in onFailed }, or: onOther)
    }
}
