using UnityEngine;

public class Singleton<T> : MonoBehaviour where T : MonoBehaviour
{
    private static T _instance;

    public static T Instance
    {
        get
        {
            if (_instance == null)
            {
                _instance = FindObjectOfType<T>();

                if (_instance == null)
                {
                    GameObject prefab = Resources.Load<GameObject>(typeof(T).Name);
                    if (prefab != null)
                    {
                        GameObject instance = Instantiate(prefab);
                        _instance = instance.GetComponent<T>();
                    }
                    else
                    {
                        Debug.LogError($"An instance of {typeof(T)} is required in the scene or Resources, but none was found.");
                    }
                }
            }
            return _instance;
        }
    }

    protected virtual void Awake()
    {
        if (_instance == null)
        {
            _instance = this as T;
            DontDestroyOnLoad(gameObject);
        }
        else if (_instance != this)
        {
            Destroy(gameObject);
        }
    }
}
