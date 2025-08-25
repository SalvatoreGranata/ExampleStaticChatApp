package com.saltech.examplechatapp.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.saltech.examplechatapp.R
import com.saltech.examplechatapp.chat.model.Message
import com.saltech.examplechatapp.chat.utils.MessageType
import com.saltech.examplechatapp.chat.utils.MessageUtils.Companion.getLatitude
import com.saltech.examplechatapp.chat.utils.MessageUtils.Companion.getLongitude
import com.saltech.examplechatapp.utils.Constants.OUTPUT_MESSAGE_DATE_PATTERN
import com.saltech.examplechatapp.utils.DateUtils.Companion.formatDate
import com.saltech.examplechatapp.utils.theme.PrimaryColor
import com.saltech.examplechatapp.utils.theme.SecondaryColor
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Calendar

@Composable
fun MapMessageItem(message: Message, sender: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = if (sender) Arrangement.Start else Arrangement.End
    ) {

        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(if (sender) PrimaryColor else SecondaryColor),
            modifier = Modifier
                .height(500.dp)
                .width(250.dp)
                .padding(5.dp)
        ) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    val mapView = MapView(context)
                    val startPoint = GeoPoint(message.content.getLatitude(), message.content.getLongitude())
                    val positionPin = Marker(mapView)
                    positionPin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    positionPin.position = startPoint
                    mapView.setTileSource(TileSourceFactory.USGS_TOPO)
                    mapView.setMultiTouchControls(true)
                    mapView.controller.setCenter(startPoint)
                    mapView.controller.setZoom(5.0)
                    mapView.overlays.add(positionPin)
                    mapView
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = if (sender) Arrangement.End else Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    text = ((if (sender) message.sentAt?.formatDate(OUTPUT_MESSAGE_DATE_PATTERN) else message.receivedAt?.formatDate(OUTPUT_MESSAGE_DATE_PATTERN))!!),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current

    MapMessageItem(
        message = Message(1, MessageType.LOCATION, context.getString(R.string.lorem_ipsum), Calendar.getInstance().time.toString(), Calendar.getInstance().time.toString()),
        sender = true
    )
}