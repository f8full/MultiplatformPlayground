package com.halcyonmobile.multiplatformplayground.ui

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.halcyonmobile.multiplatformplayground.R
import com.halcyonmobile.multiplatformplayground.model.ui.UploadApplicationUiModel
import com.halcyonmobile.multiplatformplayground.model.ui.UploadApplicationUiModelChangeListener
import com.halcyonmobile.multiplatformplayground.shared.util.ImageFile
import com.halcyonmobile.multiplatformplayground.shared.util.toImageFile
import com.halcyonmobile.multiplatformplayground.ui.shared.Screenshots
import com.halcyonmobile.multiplatformplayground.ui.theme.AppTheme
import com.halcyonmobile.multiplatformplayground.util.composables.BackBar
import com.halcyonmobile.multiplatformplayground.util.registerForActivityResult
import com.halcyonmobile.multiplatformplayground.viewmodel.UploadApplicationViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

@Composable
fun UploadApplication(upPress: () -> Unit) {
    // TODO remove initial categoryId and add category selection
    val viewModel = remember {
        UploadApplicationViewModel(1)
    }
    val uploadApplicationUiModel by viewModel.uploadApplicationUiModel.collectAsState(
        UploadApplicationUiModel(categoryId = 1)
    )
    val state by viewModel.state.collectAsState()
    val event by viewModel.event.collectAsState(null)

    if (event == UploadApplicationViewModel.Event.SUCCESSFUL_UPLOAD) {
        upPress()
    }

    val getIcon = registerForGalleryResult(viewModel::onIconChanged)
    val getScreenshot = registerForGalleryResult(viewModel::onAddScreenshot)

    Scaffold(
        topBar = { BackBar(upPress = upPress) },
        bodyContent = {
            when (state) {
                UploadApplicationViewModel.State.LOADING -> Box(
                    Modifier.fillMaxSize(),
                    Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                UploadApplicationViewModel.State.NORMAL -> ScrollableColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Card(
                        modifier = Modifier.preferredSize(88.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(51),
                        backgroundColor = AppTheme.colors.cardButton
                    ) {
                        Box(Modifier.clickable { getIcon.launchAsImageResult() }) {
                            if (uploadApplicationUiModel.icon == null) {
                                Image(
                                    imageVector = vectorResource(id = R.drawable.ic_add_image),
                                    colorFilter = ColorFilter.tint(AppTheme.colors.primary),
                                    modifier = Modifier.wrapContentSize().align(Alignment.Center)
                                )
                            } else {
                                CoilImage(
                                    data = uploadApplicationUiModel.icon!!.uri,
                                    modifier = Modifier.matchParentSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    Screenshots(
                        screenshots = uploadApplicationUiModel.screenshots,
                        onAddScreenshot = { getScreenshot.launchAsImageResult() },
                        showAdd = true
                    )
                    ApplicationDetails(uploadApplicationUiModel, viewModel)
                    ExtendedFloatingActionButton(
                        text = { Text(stringResource(R.string.submit)) },
                        onClick = viewModel::submit,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            }
        }
    )
}

private fun ActivityResultLauncher<String>.launchAsImageResult() = launch("image/*")

@Composable
private fun registerForGalleryResult(callback: (ImageFile) -> Unit) =
    (AmbientContext.current as AppCompatActivity).contentResolver.let { contentResolver ->
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.toImageFile(contentResolver)?.let(callback)
        }
    }

@Composable
private fun ApplicationDetails(
    uploadApplicationUiModel: UploadApplicationUiModel,
    changeListener: UploadApplicationUiModelChangeListener
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = stringResource(id = R.string.application_details),
            style = AppTheme.typography.h6
        )
        // TODO add category
        TextField(
            value = uploadApplicationUiModel.name,
            onValueChange = changeListener::onNameChanged,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            placeholder = { Text(text = stringResource(id = R.string.application_name)) },
            leadingIcon = {
                Image(
                    imageVector = vectorResource(id = R.drawable.ic_label),
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )
            })
        TextField(
            value = uploadApplicationUiModel.developer,
            onValueChange = changeListener::onDeveloperChanged,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(text = stringResource(id = R.string.developer)) },
            leadingIcon = {
                Image(
                    imageVector = vectorResource(id = R.drawable.ic_developer),
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )
            })
        TextField(
            value = uploadApplicationUiModel.description,
            onValueChange = changeListener::onDescriptionChanged,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(text = stringResource(id = R.string.description)) },
            leadingIcon = {
                Image(
                    imageVector = vectorResource(id = R.drawable.ic_description),
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )
            })
        Row(modifier = Modifier.navigationBarsPadding().fillMaxWidth().padding(top = 8.dp)) {
            TextField(
                value = uploadApplicationUiModel.downloads,
                onValueChange = changeListener::onDownloadsChanged,
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                placeholder = { Text(text = stringResource(id = R.string.downloads)) },
                leadingIcon = {
                    Image(
                        imageVector = vectorResource(id = R.drawable.ic_downloads),
                        colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                    )
                })
            TextField(
                value = uploadApplicationUiModel.rating?.toString() ?: "",
                onValueChange = changeListener::onRatingChanged,
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                placeholder = { Text(text = stringResource(id = R.string.rating)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = {
                    Image(
                        imageVector = vectorResource(id = R.drawable.ic_rating),
                        colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                    )
                })
        }
    }
}

@Composable
fun UploadApplicationPreview() {
    Surface(Modifier.fillMaxSize()) {
        UploadApplication { }
    }
}