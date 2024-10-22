const API_BASE_URL = 'http://localhost:8080/api/book-creatures';
export async function getBookCreatures(token) {
  const response = await fetch(`${API_BASE_URL}/all`, {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    console.error(`Error: ${response.status}, Response: ${errorText}`);
    throw new Error(`Error fetching book creatures: ${response.status}`);
  }

  try {
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Failed to parse JSON response:", error);
    return [];
  }
}


export async function getBookCreaturesWithFilters(token, name, age) {
  try {
    const url = new URL(`${API_BASE_URL}/find`);
    if (name) url.searchParams.append('name', name);
    if (age) url.searchParams.append('age', age);

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error(`Ошибка: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Ошибка при выполнении запроса:', error);
    return null;
  }
}

export async function createBookCreature(creature, token) {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(creature),
  });

  if (response.ok) {
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      return {};
    }
  } else {
    throw new Error(`Ошибка создания объекта: ${response.status}`);
  }
}

async function handleCreate(creature) {
  const token = localStorage.getItem('jwtToken');
  try {
    await createBookCreature(creature, token);
    console.log("Creature created successfully!");
  } catch (error) {
    console.error("Error creating creature:", error);
  }
}


export async function updateBookCreature(id, creature) {
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    throw new Error("Token is not available.");
  }

  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(creature),
  });

  if (response.ok) {
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    } else {
      return {};
    }
  } else {

    const errorText = await response.text();
    if (response.status === 403) {

      throw new Error("You do not have permission to update this creature.");
    } else if (response.status === 404) {
      throw new Error("Creature not found.");
    } else if (response.status === 500) {
      throw new Error("An error occurred while updating the creature. Please try again later.");
    }
    throw new Error(`Error updating object: ${response.status} - ${errorText}`);
  }
}

async function handleUpdateCreature(id, creature) {
  try {
    const updatedCreature = await updateBookCreature(id, creature);
    console.log("Creature updated successfully!", updatedCreature);
  } catch (error) {
    alert(error.message);
    console.error("Error updating creature:", error);
  }
}


export async function deleteBookCreature(id) {
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    throw new Error("Token is not available.");
  }

  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    if (response.status === 403) {
      throw new Error("You do not have permission to delete this creature.");
    } else if (response.status === 404) {
      throw new Error("Creature not found.");
    } else if (response.status === 500) {
      throw new Error("An error occurred while deleting the creature. Please try again later.");
    }
    throw new Error(`Error deleting creature: ${response.status} - ${errorText}`);
  }

  console.log("Creature deleted successfully");
  return;
}

async function handleDeleteCreature(id) {
  try {
    await deleteBookCreature(id);
    console.log("Creature deleted successfully!");
  } catch (error) {
    alert(error.message);
    console.error("Error deleting creature:", error);
  }






}
