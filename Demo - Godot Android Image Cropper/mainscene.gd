extends Control


# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass


func _on_load_img_button_pressed():
	$ImageCropper.image_data.connect(load_image_to_texture)
	$ImageCropper.setCropShape($ImageCropper.SHAPE_RECTANGLE)
	$ImageCropper.setMinCropSize(50,50)
	$ImageCropper.setMaxCropSize(1000,1000)
	$ImageCropper.setGuidelines(true)
	$ImageCropper.includeGallery(true)
	$ImageCropper.includeCamera(false)
	$ImageCropper.fixAspectRatio(true)
	$ImageCropper.compressedFormat($ImageCropper.COMPRESSED_PNG)
	$ImageCropper.getImage()

func load_image_to_texture(dick : Dictionary):
		var image = Image.new()
		if $ImageCropper.compressed_format == $ImageCropper.COMPRESSED_JPG:
			image.load_jpg_from_buffer(dick["0"])
		elif $ImageCropper.compressed_format == $ImageCropper.COMPRESSED_PNG:
			image.load_png_from_buffer(dick["0"])
		else:
			image.load_webp_from_buffer(dick["0"])
		var textrect : = TextureRect.new()
		textrect.texture = ImageTexture.create_from_image(image)
		$Control.add_child(textrect)
	
