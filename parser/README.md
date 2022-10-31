# Parser

## Idea

```
 +-------------+
 |             |
 |  Parser lib |
 |             |
 +-----+-------+
       |
 +-----+-------+
 |             |              +------------+
 |  Python API |              |            |
 |   backend   |--------------|  Frontend  |
 |             |              |            |
 +-------------+              +------------+
```

- **Parser lib**: This will parse the CAFF/CIFF files and send metadata
  and image data to the backend when it's request it.
  - **Metadata**:
    - **height**: Image height.
    - **width**: Image width.
    - **caption**: Image caption.
    - **tags**: Tags for each images.
    - **num_amin**: Number of images in an animation.
    - **credits**:
      - **date**: Creation date.
      - **creator**: Creator name.
    - **duration**: Duration of the animation.
  - **Image data**: An image pixel by pixel.
- **Python API**: If the frontend receives a request to display a CAFF file,
  it sends a request to the backend, which parses the CAFF file defined in
  the request and forwards the necessary information to the frontend.
- **Frontend**: Displays the received binary with the given parameters.

## TODO
