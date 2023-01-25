@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.matthewleeshort.takehome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
                var gifs by remember(0){ mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConstraintLayout {
                        val (button, loader, checkbox) = createRefs()
                        CircularProgressIndicator(modifier = Modifier.constrainAs(loader){
                            centerHorizontallyTo(parent)
                            centerVerticallyTo(parent)
                        })
                        GlideImage(
                            model = (if(gifs) getString(R.string.random_cat_endpoint_gif)
                                     else     getString(R.string.random_cat_endpoint_root)
                                    ).format(catCount),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(10.dp))
                                .padding(16.dp).constrainAs(checkbox){
                                    start.linkTo(parent.start, 16.dp)
                                    top.linkTo(parent.top, 16.dp)
                                }
                        ) {
                            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                Checkbox(checked = gifs, onCheckedChange = {
                                    gifs = it
                                }, modifier = Modifier.padding(end=16.dp) )
                            }
                            Text(getString(R.string.checkbox_text))
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