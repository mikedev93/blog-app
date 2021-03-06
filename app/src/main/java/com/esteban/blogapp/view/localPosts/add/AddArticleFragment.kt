package com.esteban.blogapp.view.localPosts.add

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.esteban.blogapp.R
import com.esteban.blogapp.data.LocalArticle
import com.esteban.blogapp.data.LocalArticleViewModel
import com.esteban.blogapp.utils.CameraUtil
import com.esteban.blogapp.utils.SnackbarUtil
import com.esteban.blogapp.view.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_add_article.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddArticleFragment : Fragment() {

    private lateinit var homeActivity: HomeActivity
    private lateinit var localArticleViewModel: LocalArticleViewModel

    private val REQUEST_IMAGE_CAPTURE = 111
    private val REQUEST_IMAGE_SELECTED = 222
    private var imageURI: Uri? = null
    private var imageURL: String? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            homeActivity.title = resources.getText(R.string.title_dashboard)
            homeActivity.fragmentManager.popBackStackImmediate()
            homeActivity.activeFragment = homeActivity.fragmentLocal
            this.remove()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity?.title = resources.getText(R.string.title_new_article)
        setHasOptionsMenu(true)
        localArticleViewModel = ViewModelProvider(this).get(LocalArticleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeActivity = context as HomeActivity
        btn_pick_image.setOnClickListener {
            selectImage()
        }

        btn_paste_url.setOnClickListener {
            pasteUrl()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.actionbar_new_article_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.save_btn -> {
                saveArticleToDatabase()
                return true
            }
            else -> return false
        }
    }

    private fun saveArticleToDatabase() {
        val title = et_title.text.toString()
        val content = et_content.text.toString()
        val author = "Blog App"
        var dateFormatInput: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = dateFormatInput.format(Date())
        val imagePath = imageURI?.toString()
        val imageUrl = imageURL
        if (inputCheck(title, content)) {
            val localArticle = LocalArticle(0, author, title, imagePath, imageUrl, date, content)
            localArticleViewModel.addArticle(localArticle)
            val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.nav_view)!!
            SnackbarUtil.displaySuccess(requireContext(), bottomNavigationView, "New article saved!")
            homeActivity.title = resources.getText(R.string.title_dashboard)
            homeActivity.fragmentManager.popBackStackImmediate()
            callback.remove()
        } else {
            val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.nav_view)!!
            SnackbarUtil.displayError(requireContext(), bottomNavigationView, "Missing data!")
        }
    }

    private fun inputCheck(title: String, content: String): Boolean {
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(content))
    }

    private fun selectImage() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Photo Gallery", "Camera")
        pictureDialog.setItems(pictureDialogItems) { dialog: DialogInterface?, which: Int ->
            when (which) {
                0 -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_IMAGE_SELECTED)
                }
                1 -> checkPermissionAndOpenCamera()
            }
        }
        pictureDialog.show()
    }

    private fun pasteUrl() {
        var clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (!clipboardManager.hasPrimaryClip()) {
            //Toast something
        } else if (!clipboardManager.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            //Toast something
        } else {
            var item = clipboardManager.primaryClip?.getItemAt(0)
            imageURL = item?.text.toString()
            Glide.with(requireContext())
                .load(imageURL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        if (progress_bar != null) {
                            progress_bar.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        if (progress_bar != null) {
                            progress_bar.visibility = View.GONE
                        }
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView)
            clearImageURI()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    CameraUtil.createImageFile(requireContext())
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.app.android.fileprovider",
                        it
                    )
                    imageURI = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    fun checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
        } else {
            dispatchTakePictureIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Glide.with(requireContext())
                .load(imageURI)
                .centerCrop()
                .into(imageView)
            clearImageURL()
        } else if (requestCode == REQUEST_IMAGE_SELECTED && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val imagePath = data.data
                Glide.with(requireContext())
                    .load(imagePath)
                    .centerCrop()
                    .into(imageView)
                clearImageURL()
                imageURI = imagePath
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.onBackPressedDispatcher!!.addCallback(callback)
    }

    private fun clearImageURI(){
        imageURI = null
    }

    private fun clearImageURL() {
        imageURL = null
    }
}