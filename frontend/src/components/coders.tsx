import { useEffect, useState } from "react";


interface ICoder {
    id: number | null;
    name: string;
}

const Coders = () => {
    const [newCoder, setNewCoder] = useState<ICoder>();
    const [coders, setCoders] = useState<ICoder[]>([]);

    useEffect(() => {
        loadCoders();
    }, [])

    const loadCoders = () => {
        fetch('/api/coders') // proxy will forward this to Spring Boot
            .then(res => res.json())
            .then((data: ICoder[]) => setCoders(data));
    }

    const addCoder = () => {
        if (!newCoder) {
            return;
        }
        fetch('/api/coders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newCoder),
        }).then(() => {
            loadCoders();
            setNewCoder({ id: null, name: "" });
        });
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNewCoder({ id: null, name: e.target.value });
    }

    return (
        <div className='coders'>
            <h1>Coders</h1>
            <ul>
                {coders.map((u) => (
                    <li key={u.id}>{u.name}</li>
                ))}
            </ul>
            <input type='text' value={newCoder?.name} onChange={handleChange} />
            <button onClick={addCoder}>Add</button>
        </div>
    );
}

export default Coders;