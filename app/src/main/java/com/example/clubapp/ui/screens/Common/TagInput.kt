package com.example.clubapp.ui.screens.Common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagInput(
    modifier: Modifier = Modifier,
    maxTags: Int = 3,
    onTagsChanged: (List<String>) -> Unit
) {
    var tagText by remember { mutableStateOf(TextFieldValue("")) }
    var tagList by remember { mutableStateOf(listOf<String>()) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text("Tags")
        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tagList.forEach { tag ->
                TagChip(text = tag) {
                    tagList = tagList - tag
                    onTagsChanged(tagList)
                }
            }

            if (tagList.size < maxTags) {
                OutlinedTextField(
                    value = tagText,
                    onValueChange = { tagText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent {
                            if (it.type == KeyEventType.KeyUp && it.key == Key.Enter) {
                                val trimmed = tagText.text.trim()
                                if (trimmed.isNotEmpty() && trimmed !in tagList) {
                                    tagList = tagList + trimmed
                                    onTagsChanged(tagList)
                                    tagText = TextFieldValue("")
                                }
                                true
                            } else false
                        },
                    placeholder = { Text("Add tag") },
                    singleLine = true,
                    trailingIcon = {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear text",
                            modifier = Modifier.clickable { tagText = TextFieldValue("") }
                        )
                    }
                )
            }
        }

        // Add button to manually add tags
        if (tagText.text.isNotEmpty() && tagList.size < maxTags) {
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Button(
                onClick = {
                    val trimmed = tagText.text.trim()
                    if (trimmed.isNotEmpty() && trimmed !in tagList) {
                        tagList = tagList + trimmed
                        onTagsChanged(tagList)
                        tagText = TextFieldValue("")
                    }
                }
            ) {
                Text("Add Tag")
            }
        }

        if (tagList.size >= maxTags) {
            Text(
                text = "Max $maxTags tags allowed",
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
@Composable
fun TagChip(
    text: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "#$text")
            Spacer(modifier = Modifier.width(4.dp))
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove tag",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

