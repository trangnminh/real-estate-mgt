import React,{useState} from 'react'
import axios from 'axios'

export default function AddHouse() {
    const [name,setName] = useState("")
    const [image,setImage] = useState([])

    const nameOnChange = (e) => {
        setName(e.target.value)
    }

    const handleChange = (e) =>{
        const files = Array.from(e.target.files)
        setImage(files)
    }

    const handleUploadUsingS3 = async () =>{
        const formData = new FormData()
        image.forEach(file =>{
            formData.append('files',file)
        })
        formData.append('name',name)
      
        await axios.post('http://localhost:8080/api/v1/houses',formData)
        .then((res)=>{
          console.log(res.data)
        })
        .catch(error =>console.log(error)) 
      }

    return (
        <div className="add-house">
            <input type="file" multiple onChange={handleChange}/>
            <br/>
            <input type="text" placeholder="house name" value={name} onChange={nameOnChange} required/>
            <button onClick={handleUploadUsingS3}>Upload using s3</button>
        </div>
    )
}
