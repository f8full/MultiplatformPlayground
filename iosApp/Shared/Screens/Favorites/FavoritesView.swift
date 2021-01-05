//
//  FavoritesView.swift
//  iosApp
//
//  Created by Zsolt Boldizsar on 9/10/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import common

struct FavoritesView: View {
    
    @ObservedObject var state: FavoritesState
    
    init() {
        state = FavoritesState()
    }
    
    var body: some View {
        NavigationView {
            switch state.state {
            case FavouritesViewModel.State.loading:
                ProgressView()
            case FavouritesViewModel.State.error:
                PlaceholderView(message: MR.strings().general_error.localize()) {
                    state.viewModel.loadFavourites()
                }
            default:
                List(state.favourites, id: \.id){ favourite in
                    NavigationLink(destination: ApplicationDetailView(applicationId: favourite.id)){
                        ApplicationView(application: favourite)
                    }
                }
                .navigationTitle(MR.strings().favourites.localize())
            }
        }
    }
}

struct FavoritesView_Previews: PreviewProvider {
    
    static var previews: some View {
        FavoritesView()
    }
}
