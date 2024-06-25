@tool
class_name GodotImageCropper
extends Node

const IMAGE_LOADED: String = "image_loaded";
const PLUGIN_SINGLETON_NAME: String = "GodotImageCropperPlugin"
const USER_CANCELLED = "user_cancelled"
const SHAPE_OVAL = "OVAL"
const SHAPE_RECTANGLE = "RECTANGLE"
const COMPRESSED_JPG = "JPEG"
const COMPRESSED_PNG = "PNG"
const COMPRESSED_WEBP = "WEBP"
var compressed_format = "PNG"
signal image_data
signal user_cancelled_operation

var _plugin_singleton: Object
func _ready() -> void:
	_update_plugin()


func _notification(a_what: int) -> void:
	if a_what == NOTIFICATION_APPLICATION_RESUMED:
		_update_plugin()


func _update_plugin() -> void:
	if _plugin_singleton == null:
		if Engine.has_singleton(PLUGIN_SINGLETON_NAME):
			_plugin_singleton = Engine.get_singleton(PLUGIN_SINGLETON_NAME)
			compressedFormat(COMPRESSED_PNG)
			_connect_signals()
		else:
			printerr("%s singleton not found!" % PLUGIN_SINGLETON_NAME)

func getImage():
	if _plugin_singleton != null:
		_plugin_singleton.getImage()

func setCropShape(shape : String):
	if _plugin_singleton != null:
		_plugin_singleton.setCropShape(shape)

func setMinCropSize(minWidth : int, minHeight : int):
	if _plugin_singleton != null:
		_plugin_singleton.setMinCropSize(minWidth, minHeight)

func setMaxCropSize(maxWidth : int, maxHeight : int):
	if _plugin_singleton != null:
		_plugin_singleton.setMaxCropSize(maxWidth, maxHeight)

func setGuidelines(b : bool):
	if _plugin_singleton != null:
		_plugin_singleton.setGuidelines(b)

func includeGallery(b: bool):
	if _plugin_singleton != null:
		_plugin_singleton.includeGallery(b)

func includeCamera(b: bool):
	if _plugin_singleton != null:
		_plugin_singleton.includeCamera(b)

func fixAspectRatio(b:bool):
	if _plugin_singleton != null:
		_plugin_singleton.fixAspectRatio(b)

func compressedFormat(s : String):
	if _plugin_singleton != null:
		_plugin_singleton.compressedFormat(s)
		compressed_format = s

func setAspectRatio (x : int, y:int):
	if _plugin_singleton != null:
		_plugin_singleton.setAspectRatio(x,y)

func _connect_signals() -> void:
	_plugin_singleton.connect(IMAGE_LOADED, on_image_loaded)
	_plugin_singleton.connect(USER_CANCELLED, on_user_cancelled)

func on_user_cancelled():
	user_cancelled_operation.emit()

func on_image_loaded(dick):
	image_data.emit(dick)
