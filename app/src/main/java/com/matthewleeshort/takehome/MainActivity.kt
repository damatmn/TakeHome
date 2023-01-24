@file:OptIn(ExperimentalGlideComposeApi::class)

package com.matthewleeshort.takehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.matthewleeshort.takehome.ui.theme.TakeHomeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TakeHomeTheme {

                var catCount by remember(0){ mutableStateOf(0) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConstraintLayout {
                        val (button) = createRefs()
                        GlideImage(
                            model = getString(R.string.random_cat_endpoint_root).format(catCount),
                            contentDescription = getString(R.string.cat_content_desc),
                            ) {
                            it.diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                        }
                        Button(onClick = {
                            catCount += 1
                        }, modifier = Modifier.constrainAs(button) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }) {
                            Text(getString(R.string.new_cat_button))
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TakeHomeTheme {}
}