//
//  ApplicationsView.swift
//  iosApp
//
//  Created by Nagy Robert on 04/11/2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import common

struct ApplicationsView: View {
    
    let categoryId: Int64
    @ObservedObject var state: ApplicationsState
    
    init(categoryId: Int64) {
        self.categoryId =  categoryId
        state = ApplicationsState(categoryId: categoryId)
    }
    
    var body: some View {
        switch state.state {
        case ApplicationsViewModel.State.error:
            PlaceholderView(message: MR.strings().general_error.localize())
        case ApplicationsViewModel.State.empty:
            PlaceholderView(message: MR.strings().applications_empty_message.localize())
        default:
            List(state.items, id: \.id) { item in
                switch item {
                case let app as ApplicationUiModel.App:
                    NavigationLink(destination: ApplicationDetailView(applicationId: app.id)) {
                        ApplicationView(application: app)
                    }.onAppear{
                        if(app.id == state.items.last?.id){
                            state.viewModel.load()
                        }
                    }
                default:
                    HStack{
                        Spacer()
                        ProgressView()
                        Spacer()
                    }
                }
            }
            .listStyle(PlainListStyle())
        }
    }
}
