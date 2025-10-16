using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class EconomyManager : Singleton<EconomyManager>
{
    [SerializeField] private TMP_Text goldText; // Assign this from the Inspector
    private int currentGold = 0;

    private void Start()
    {
        // Optional fallback if not assigned manually
        if (goldText == null)
        {
            GameObject goldObj = GameObject.Find("Gold Amount Text");
            if (goldObj != null)
            {
                goldText = goldObj.GetComponent<TMP_Text>();
            }
            else
            {
                Debug.LogError("Gold Amount Text UI element not found in scene!");
            }
        }

        UpdateGoldText();
    }

    public void UpdateCurrentGold()
    {
        currentGold += 1;
        UpdateGoldText();
    }

    private void UpdateGoldText()
    {
        if (goldText != null)
        {
            goldText.text = currentGold.ToString("D3");
        }
        else
        {
            Debug.LogWarning("Gold text is not assigned.");
        }
    }
}
