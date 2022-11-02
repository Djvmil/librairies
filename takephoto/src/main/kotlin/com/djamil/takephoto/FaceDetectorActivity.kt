//package com.djamil.takephoto
//
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.util.Log
//import androidx.appcompat.app.AppCompatActivity
//import com.djamil.takephoto.facedetector.*
//import com.otaliastudios.cameraview.Facing
//import kotlinx.android.synthetic.main.activity_face_detector.*
//
//class FaceDetectorActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_face_detector)
//
//        val lensFacing = savedInstanceState?.getSerializable(KEY_LENS_FACING) as Facing? ?: Facing.BACK
//        setupCamera(lensFacing)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        viewfinder.start()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        viewfinder.stop()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        outState.putSerializable(KEY_LENS_FACING, viewfinder.facing)
//        super.onSaveInstanceState(outState, outPersistentState)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        viewfinder.destroy()
//    }
//
//    private fun setupCamera(lensFacing: Facing) {
//        val faceDetector = FaceDetector(faceBoundsOverlay)
//        viewfinder.facing = lensFacing
//        viewfinder.addFrameProcessor {
//            val width = if(it.size?.width != null ) it.size?.width else 0
//            val height = if(it.size?.height != null ) it.size?.height else 0
//            faceDetector.process(
//                Frame(
//                    data = it.data,
//                    rotation = it.rotation,
//                    size = SizeUtil(width!!, height!!),
//                    format = it.format,
//                    lensFacing = if (viewfinder.facing == Facing.BACK) LensFacing.BACK else LensFacing.FRONT
//                )
//            )
//            faceDetector.setonFaceDetectionFailureListener(object: FaceDetector.OnFaceDetectionResultListener{
//                override fun onSuccess(faceBounds: FaceBounds) {
//                    super.onSuccess(faceBounds)
//                    Log.e(TAG, "onSuccess: detected")
//                }
//
//                override fun onFailure(exception: Exception) {
//                    super.onFailure(exception)
//                    Log.e(TAG, "onFailure: not detected")
//                }
//            })
//
//        }
//        toggleCameraButton.setOnClickListener {
//            viewfinder.toggleFacing()
//        }
//    }
//
//    companion object {
//        private const val TAG = "MainActivity"
//        private const val KEY_LENS_FACING = "key-lens-facing"
//    }
//}
