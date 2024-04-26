package uk.ac.tees.mad.d3901263.screens.profile

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3901263.R
import uk.ac.tees.mad.d3901263.navigation.Navigation
import uk.ac.tees.mad.d3901263.ui.theme.smokeWhite

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val uiState by profileViewModel.profileUiState.collectAsState()
    val getUserState by profileViewModel.currentUserState.collectAsState(initial = null)
    val updateProfile by profileViewModel.userUpdateState.collectAsState(initial = null)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    //Taking images from gallary
    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImage = uri
            if (uri != null) {
                profileViewModel.handleImageSelection(uri, context)
            }
        }

    val requestCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let { profileViewModel.handleImageCapture(it) }
        }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
    ) {
        ProfileHeader(
            onBack = onBack,
            title = "Edit profile"
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                windowInsets = WindowInsets.ime
            ) {
                // Sheet content
                PhotoPickerSheet(
                    openGallery = {
                        coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        galleryLauncher.launch("image/*")
                    },
                    openCamera = {
                        coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                        if (cameraPermission.status.isGranted) {
                            requestCameraLauncher.launch(null)
                        } else {
                            cameraPermission.launchPermissionRequest()
                            if (cameraPermission.status.isGranted) {
                                requestCameraLauncher.launch(null)
                            }
                        }
                    }
                )
            }
        }
        if (getUserState?.isLoading == true) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UploadPhoto(onClick = { showBottomSheet = true }, image = uiState.image)
                Spacer(modifier = Modifier.height(24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextFieldComponent(
                        label = "Name",
                        text = uiState.name,
                        onChange = { profileViewModel.updateProfileUiState(uiState.copy(name = it)) },
                        focusManager = focusManager
                    )
                    TextFieldComponent(
                        label = "Phone Number",
                        text = uiState.phone,
                        onChange = { profileViewModel.updateProfileUiState(uiState.copy(phone = it)) },
                        focusManager = focusManager
                    )
                    TextFieldComponent(
                        label = "Date of Birth",
                        text = uiState.dob,
                        onChange = { profileViewModel.updateProfileUiState(uiState.copy(dob = it)) },
                        focusManager = focusManager
                    )
                    TextFieldComponent(
                        label = "Gender",
                        text = uiState.gender,
                        onChange = { profileViewModel.updateProfileUiState(uiState.copy(gender = it)) },
                        focusManager = focusManager,
                        isLastField = true
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { profileViewModel.updateUser() }, modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (updateProfile?.isLoading == true) {
                        CircularProgressIndicator(color = smokeWhite)
                    } else {
                        Text(text = "Update profile")
                    }
                }
            }
        }
        LaunchedEffect(key1 = updateProfile) {
            updateProfile?.data?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                onBack()
            }
            updateProfile?.error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

@Composable
fun TextFieldComponent(
    label: String,
    text: String,
    onChange: (String) -> Unit,
    isLastField: Boolean = false,
    focusManager: FocusManager
) {
    Column {
        Text(text = label)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            colors = TextFieldDefaults.colors(

            ),
            placeholder = {
                Text(text = label)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = if (isLastField) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
    }
}

@Composable
fun UploadPhoto(
    onClick: () -> Unit,
    image: ByteArray?
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .height(100.dp)
            .aspectRatio(1f),
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (image == null) {
                Image(
                    imageVector = Icons.Rounded.AddAPhoto,
                    contentDescription = "Upload image",
                    modifier = Modifier.size(40.dp)
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .crossfade(true)
                        .data(image)
                        .build(),
                    contentDescription = "Selected image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

        }
    }
}

@Composable
fun PhotoPickerSheet(
    openCamera: () -> Unit,
    openGallery: () -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .clickable {
                    openCamera()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CameraEnhance,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )

            Text(
                text = "Camera",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .clickable {
                    openGallery()
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Image, contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(35.dp)
            )
            Text(
                text = "Gallery",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
    }
}

object EditProfile : Navigation {
    override val route = "edit_profile"
    override val titleRes: Int = R.string.app_name
}