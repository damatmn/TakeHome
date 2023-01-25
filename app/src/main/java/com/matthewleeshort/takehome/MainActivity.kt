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

/**
 * Main and only activity for an application displaying cats from https://cataas.com
 *
 * Currently loads one image at a time using Glide's compose integration with the option to limit
 * the images to gifs.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TakeHomeTheme {

                /*
                * cat count acts as state holder to prompt the GlideImage composable to reload the
                * model, setting other properties will recompose GlideImage however it will not
                * redraw without the model change.
                * */
                var catCount by remember(0){ mutableStateOf(0) }

                // A state holder to manage the gifs checkbox.
                var gifs by remember(0){ mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConstraintLayout {
                        val (button, loader, checkbox) = createRefs()

                        /*
                         * This progress bar is always enabled though covered whenever there is an
                         * image to display
                         * */
                        CircularProgressIndicator(modifier = Modifier.constrainAs(loader){
                            centerHorizontallyTo(parent)
                            centerVerticallyTo(parent)
                        })
                        GlideImage(
                            /*
                             * Setting the model here switches between the two modes (regular and
                             * gif only) and also includes the catCount forcing the reload when it
                             * is incremented
                             * */
                            model = (if(gifs) getString(R.string.random_cat_endpoint_gif)
                                     else     getString(R.string.random_cat_endpoint_root)
                                    ).format(catCount),
                            contentDescription = getString(R.string.cat_content_desc),
                            ) {
                            /*
                             * We also need to scrap oud cache given we are hitting the same
                             * endpoint each time.
                             * */
                            it.diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                        }

                        // A button for incrementing the catCount
                        Button(onClick = {
                            catCount += 1
                        }, modifier = Modifier.constrainAs(button) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            start.linkTo(parent.start, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }) {
                            Text(getString(R.string.new_cat_button))
                        }

                        /*
                         * A row housing our checkbox and text with a background that makes it a bit
                         * easier to see and and is rounded because the square background looked
                         * even jankier.
                         * */
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(16.dp)
                                .constrainAs(checkbox) {
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