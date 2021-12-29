import React from 'react'
import GoogleMapReact from 'google-map-react'
import { Icon } from '@iconify/react'
import locationIcon from '@iconify/icons-mdi/map-marker'
import './map.css'
require('dotenv').config()

const api_key = process.env.REACT_APP_API_KEY_GOOGLE_MAPS
const LocationPin = ({ text }) => (
  <div className="pin">
    <Icon icon={locationIcon} className="pin-icon" style={{ width: '5rem' }} />
    <p className="pin-text">{text}</p>
  </div>
)

const Map = ({ location, zoomLevel }) => (
  <div className="map">

    <div className="google-map">
      <GoogleMapReact
        bootstrapURLKeys={{ key: api_key }}
        center={location}
        defaultZoom={zoomLevel}
        options={{ scrollwheel: false}}
      >
        <LocationPin
          lat={location.lat}
          lng={location.lng}
          text={location.address}
        />
      </GoogleMapReact>
    </div>
  </div>
)

export default Map